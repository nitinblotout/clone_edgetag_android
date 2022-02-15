package com.edgetag

import android.app.Application
import android.util.Log
import com.android.installreferrer.api.ReferrerDetails
import com.edgetag.data.database.EventDatabase
import com.edgetag.deviceinfo.device.DeviceInfo
import com.edgetag.network.HostConfiguration
import com.edgetag.network.RemoteApiClient
import com.edgetag.network.RemoteApiDataSource
import com.edgetag.network.RemoteApiService
import com.edgetag.referral.InstallRefferal
import com.edgetag.repository.EventRepository
import com.edgetag.repository.ManifestRepository
import com.edgetag.repository.data.ConfigurationDataManager
import com.edgetag.repository.data.SharedPreferenceSecureVault
import com.edgetag.repository.impl.DataManagerImpl
import com.edgetag.util.Constant
import com.edgetag.util.DateTimeUtils

class DependencyInjectorImpl private constructor(
    application: Application,
    secureStorageService: SharedPreferenceSecureVault,
    hostConfiguration: HostConfiguration,
    eventDatabase: EventDatabase) : DependencyInjector {


    private val mSecureStorageService = secureStorageService
    private val mHostConfiguration = hostConfiguration
    private val mEventDatabase = eventDatabase
    private val mApplication = application
    var mReferrerDetails: ReferrerDetails? = null



    companion object {
        private const val TAG ="DependencyInjectorImpl"
        private lateinit var instance: DependencyInjectorImpl
        private var sessionID :Long = 0
        private lateinit var eventRepository :EventRepository

        @Synchronized
        fun init(
                application: Application,
                secureStorageService: SharedPreferenceSecureVault,
                hostConfiguration: HostConfiguration
        ) :Boolean{
            try {
                instance = DependencyInjectorImpl(application, secureStorageService, hostConfiguration, EventDatabase.invoke(application))
            }catch (e:Exception){
                Log.d(TAG,e.localizedMessage!!)
                return false
            }
            return true
        }

        fun getInstance(): DependencyInjectorImpl {
            return instance
        }

        fun getSessionId():Long{
            return sessionID
        }

        fun getEventRepository():EventRepository{
            return eventRepository
        }
    }

    private val dataManager : DataManagerImpl by lazy{
        DataManagerImpl(this)
    }
    private val mManifestRepository : ManifestRepository by lazy{
        ManifestRepository(dataManager.getConfigurationDataManager())
    }

    override fun getRemoteAPIDataSource(): RemoteApiDataSource {
        return RemoteApiDataSource(RemoteApiClient(getHostService()).getRemoteApiService())
    }

    override fun getHostService():HostConfiguration{
        return mHostConfiguration

    }

    override fun getApplication():Application {
        return mApplication
    }

    override fun getConfigurationManager(): ConfigurationDataManager {
        return dataManager.getConfigurationDataManager()
    }

    override fun getEventDatabase(): EventDatabase = mEventDatabase

    override fun getManifestRepository(): ManifestRepository = mManifestRepository

    override fun getSecureStorageService(): SharedPreferenceSecureVault {
        return mSecureStorageService
    }

    fun initialize(){
        try {
            sessionID = DateTimeUtils().get13DigitNumberObjTimeStamp()
            eventRepository = EventRepository(mSecureStorageService)
            val activityLifeCycleCallback =
                AnalyticsActivityLifecycleCallbacks(eventRepository, mSecureStorageService)
            mApplication.registerActivityLifecycleCallbacks(activityLifeCycleCallback)
            eventRepository.publishEvent()
            InstallRefferal().startClient(mApplication)
            DeviceInfo(mApplication).getAdvertisingId()
        }catch (e:Throwable){
            Log.d(TAG,e.localizedMessage!!)
        }
    }
}
