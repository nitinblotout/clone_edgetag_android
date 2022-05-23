package com.edgetag

import android.app.Application
import android.content.Context
import android.util.Log
import com.edgetag.data.database.EventDatabase
import com.edgetag.model.CompletionHandler
import com.edgetag.model.ErrorCodes
import com.edgetag.model.Result
import com.edgetag.model.edgetag.ManifestConfigurationResponse
import com.edgetag.network.ApiDataProvider
import com.edgetag.network.HostConfiguration
import com.edgetag.repository.EventRepository
import com.edgetag.repository.impl.SharedPreferenceSecureVaultImpl
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call

open class EdgeTagInternal : EdgeTagInterface {

    companion object {
        private const val TAG = "AnalyticsInternal"
        private var isSdkinitiliazed = false
    }


    override fun init(
        application: Application,
        edgeTagConfiguration: EdgeTagConfiguration,
        completionHandler: CompletionHandler
    ) {
        val handler = CoroutineExceptionHandler { _, exception ->
            completionHandler.onError(
                code = ErrorCodes.ERROR_CODE_NETWORK_ERROR,
                msg = exception.localizedMessage!!
            )
        }
        CoroutineScope(Dispatchers.IO + handler).launch {

            val secureVault = SharedPreferenceSecureVaultImpl(
                application.getSharedPreferences(
                    "vault",
                    Context.MODE_PRIVATE
                ), "crypto"
            )
            val hostConfiguration = HostConfiguration(baseUrl = edgeTagConfiguration.endPointUrl)

            DependencyInjectorImpl.init(
                application = application,
                secureStorageService = secureVault,
                hostConfiguration = hostConfiguration, EventDatabase.invoke(application)
            )
            when (edgeTagConfiguration.validateRequest()) {

                ErrorCodes.ERROR_CODE_END_POINT_URL_NOT_PROPER -> {
                    completionHandler.onError(
                        code = ErrorCodes.ERROR_CODE_END_POINT_URL_NOT_PROPER,
                        msg = "End point url is invalid"
                    )
                }
                else -> {
                    DependencyInjectorImpl.getInstance().getManifestRepository()
                        .fetchManifestConfiguration(object :
                            ApiDataProvider<ManifestConfigurationResponse?>() {
                            override fun onFailed(
                                errorCode: Int,
                                message: String,
                                call: Call<ManifestConfigurationResponse?>
                            ) {
                                completionHandler.onError(code = errorCode, msg = message)
                            }

                            override fun onError(
                                t: Throwable,
                                call: Call<ManifestConfigurationResponse?>
                            ) {
                                completionHandler.onError(
                                    code = ErrorCodes.ERROR_CODE_NETWORK_ERROR,
                                    msg = t.localizedMessage!!
                                )
                            }

                            override fun onSuccess(data: ManifestConfigurationResponse?) {
                                isSdkinitiliazed = true
                                DependencyInjectorImpl.getInstance().initialize()
                                validateDisableConsentCheck(edgeTagConfiguration.disableConsentCheck)
                                completionHandler.onSuccess()
                            }

                        })
                }
            }
        }
    }

    private fun validateDisableConsentCheck(disableConsentCheck: Boolean) {
        if (disableConsentCheck) {
            val consent = hashMapOf<String, Boolean>()
            consent.put("all", true)
            consent(consent, object : CompletionHandler {
                override fun onSuccess() {
                }

                override fun onError(code: Int, msg: String) {
                }

            })
        }
    }

    @Synchronized
    override fun consent(
        consentInfo: HashMap<String, Boolean>,
        completionHandler: CompletionHandler
    ) {
        if(isSdkinitiliazed) {
            CoroutineScope(Dispatchers.Default).launch {
                try {

                    val eventsRepository =
                        EventRepository(
                            DependencyInjectorImpl.getInstance().getSecureStorageService()
                        )
                    val result = consentInfo.let {
                        eventsRepository.prepareConsent(
                            consentInfo = consentInfo
                        )
                    }
                    when (result) {
                        is Result.Success -> completionHandler.onSuccess()
                        is Result.Error -> completionHandler.onError(
                            code = result.errorData.code,
                            msg = result.errorData.msg
                        )
                    }
                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                    completionHandler.onError(
                        code = ErrorCodes.ERROR_CODE_CONSENT_ERROR,
                        msg = e.localizedMessage ?: ""
                    )
                }
            }
        }else{
            completionHandler.onError(
                code = ErrorCodes.ERROR_CODE_SDK_NOT_ENABLED,
                msg = "SDK is not initialized"
            )
        }
    }

    override fun tag(
        eventName: String,
        tagInfo: HashMap<String, Any>?,
        providerInfo: HashMap<String, Boolean>?,
        completionHandler: CompletionHandler
    ) {
        if(isSdkinitiliazed) {
            CoroutineScope(Dispatchers.Default).launch {
                try {

                    val eventsRepository =
                        EventRepository(
                            DependencyInjectorImpl.getInstance().getSecureStorageService()
                        )
                    val result = tagInfo?.let {
                        eventsRepository.prepareTagEvent(
                            tagName = eventName,
                            tagInfo = tagInfo,
                            providerInfo = providerInfo!!
                        )
                    }
                    when (result) {
                        is Result.Success -> completionHandler.onSuccess()
                        is Result.Error -> completionHandler.onError(
                            code = result.errorData.code,
                            msg = result.errorData.msg
                        )
                    }
                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                    completionHandler.onError(
                        code = ErrorCodes.ERROR_CODE_TAG_ERROR,
                        msg = e.localizedMessage ?: ""
                    )
                }
            }
        }else{
            completionHandler.onError(
                code = ErrorCodes.ERROR_CODE_SDK_NOT_ENABLED,
                msg = "SDK is not initialized"
            )
        }
    }

    override fun user(
        key: String,
        value: String,
        completionHandler: CompletionHandler
    ) {
        if (isSdkinitiliazed) {
            CoroutineScope(Dispatchers.Default).launch {
                try {

                    val eventsRepository =
                        EventRepository(
                            DependencyInjectorImpl.getInstance().getSecureStorageService()
                        )
                    val result = eventsRepository.prepareUser(
                        key = key,
                        value = value
                    )
                    when (result) {
                        is Result.Success -> completionHandler.onSuccess()
                        is Result.Error -> completionHandler.onError(
                            code = result.errorData.code,
                            msg = result.errorData.msg
                        )
                    }
                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                    completionHandler.onError(
                        code = ErrorCodes.ERROR_CODE_TAG_ERROR,
                        msg = e.localizedMessage ?: ""
                    )
                }
            }
        } else {
            completionHandler.onError(
                code = ErrorCodes.ERROR_CODE_SDK_NOT_ENABLED,
                msg = "SDK is not initialized"
            )
        }
    }
}