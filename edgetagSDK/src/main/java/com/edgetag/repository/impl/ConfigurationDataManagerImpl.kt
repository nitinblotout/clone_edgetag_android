package com.edgetag.repository.impl

import com.edgetag.model.*
import com.edgetag.model.edgetag.EdgetagMetaData
import com.edgetag.model.edgetag.ManifestConfigurationResponse
import com.edgetag.network.ApiDataProvider
import com.edgetag.network.RemoteApiDataSource
import com.edgetag.repository.data.ConfigurationDataManager

class ConfigurationDataManagerImpl(private val dataSource: RemoteApiDataSource) :
  ConfigurationDataManager {


  override suspend fun downloadManifestConfiguration(): Result<ManifestConfigurationResponse?> {
    return dataSource.getSDKManifest()
  }

  override fun publishEvents(events: EdgetagMetaData, handler: ApiDataProvider<Any?>) {
    dataSource.postEvents(events).enqueue(handler)
  }

  override fun publishConsentEvents(events: EdgetagMetaData, handler: ApiDataProvider<Any?>) {
    dataSource.postConsentEvents(events).enqueue(handler)
  }

}
