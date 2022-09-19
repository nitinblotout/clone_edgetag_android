package com.edgetag

import android.app.Application
import android.content.Context
import com.edgetag.data.database.EventDatabase
import com.edgetag.network.HostConfiguration
import com.edgetag.network.RemoteApiDataSource
import com.edgetag.network.RemoteApiService
import com.edgetag.repository.ManifestRepository
import com.edgetag.repository.data.ConfigurationDataManager
import com.edgetag.repository.data.SharedPreferenceSecureVault
import java.lang.Appendable

interface DependencyInjector {

    fun getRemoteAPIDataSource():RemoteApiDataSource
    fun getManifestRepository():ManifestRepository
    fun getSecureStorageService() : SharedPreferenceSecureVault
    fun getHostService():HostConfiguration
    fun getApplication(): Application
    fun getConfigurationManager() : ConfigurationDataManager
    fun getEventDatabase(): EventDatabase
}
