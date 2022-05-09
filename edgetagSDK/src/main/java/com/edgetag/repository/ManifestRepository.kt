package com.edgetag.repository

import com.edgetag.DependencyInjectorImpl
import com.edgetag.model.ErrorCodes.ERROR_CODE_MANIFEST_NOT_AVAILABLE
import com.edgetag.model.Result
import com.edgetag.model.edgetag.ManifestConfigurationResponse
import com.edgetag.network.ApiDataProvider
import com.edgetag.repository.data.ConfigurationDataManager
import com.edgetag.util.Constant
import com.google.gson.Gson
import retrofit2.Call

class ManifestRepository(private val configurationDataManager: ConfigurationDataManager) {


    lateinit var manifestConfigurationResponse:ManifestConfigurationResponse

    fun fetchManifestConfiguration(callback: ApiDataProvider<ManifestConfigurationResponse?>) {
        configurationDataManager.downloadManifestConfiguration(object : ApiDataProvider<ManifestConfigurationResponse?>() {
            override fun onFailed(
                errorCode: Int,
                message: String,
                call: Call<ManifestConfigurationResponse?>
            ) {
                val manifestConfiguration = getLocalManifestData()
                manifestConfiguration?.let {
                    initManifestConfiguration(manifestConfiguration)
                }?: run {
                    callback.onFailed(errorCode = ERROR_CODE_MANIFEST_NOT_AVAILABLE,message="",call)
                }
            }

            override fun onError(t: Throwable, call: Call<ManifestConfigurationResponse?>) {
                val manifestConfiguration = getLocalManifestData()
                manifestConfiguration?.let {
                    initManifestConfiguration(manifestConfiguration)
                }?: run {
                    callback.onError(t,call)
                }
            }

            override fun onSuccess(data: ManifestConfigurationResponse?) {
                initManifestConfiguration(data!!)
                callback.onSuccess(data)
            }
        })
    }

    private fun getLocalManifestData(): ManifestConfigurationResponse? {
        return Gson().fromJson(
            DependencyInjectorImpl.getInstance().getSecureStorageService()
                .fetchString(Constant.MANIFEST_DATA),
            ManifestConfigurationResponse::class.java
        )

    }

    fun initManifestConfiguration(manifestResponse: ManifestConfigurationResponse):Result<String> {
        manifestConfigurationResponse = manifestResponse
        return Result.Success("")
    }
}
