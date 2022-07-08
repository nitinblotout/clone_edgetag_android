package com.edgetag.repository.impl

import com.edgetag.model.edgetag.EdgetagMetaData
import com.edgetag.model.edgetag.ManifestConfigurationResponse
import com.edgetag.network.ApiDataProvider
import com.edgetag.network.RemoteApiDataSource
import com.edgetag.repository.data.ConfigurationDataManager

class ConfigurationDataManagerImpl(private val dataSource: RemoteApiDataSource) :
  ConfigurationDataManager {


  override fun downloadManifestConfiguration(disableConsentCheck:Boolean,handler: ApiDataProvider<ManifestConfigurationResponse?>){
    return dataSource.getSDKManifest(disableConsentCheck).enqueue(handler)
  }

  override fun publishEvents(events: EdgetagMetaData, handler: ApiDataProvider<Any?>) {
    dataSource.postEvents(events).enqueue(handler)
  }

  override fun publishConsentEvents(events: EdgetagMetaData, handler: ApiDataProvider<Any?>) {
    dataSource.postConsentEvents(events).enqueue(handler)
  }

  override fun publishUserEvents(events: EdgetagMetaData, handler: ApiDataProvider<Any?>) {
    dataSource.postUserEvents(events).enqueue(handler)
  }

  override fun publishDataEvents(events: EdgetagMetaData, handler: ApiDataProvider<Any?>) {
    dataSource.postDataEvents(events).enqueue(handler)
  }

  override fun getDataEvents(keys:String, handler: ApiDataProvider<Any?>) {
    dataSource.getDataEvents(keys).enqueue(handler)
  }

  override fun getKeyEvents(handler: ApiDataProvider<Any?>) {
    dataSource.getKeys().enqueue(handler)
  }

}
