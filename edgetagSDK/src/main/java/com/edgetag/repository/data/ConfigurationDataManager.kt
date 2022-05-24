package com.edgetag.repository.data

import com.edgetag.model.edgetag.EdgetagMetaData
import com.edgetag.model.edgetag.ManifestConfigurationResponse
import com.edgetag.network.ApiDataProvider

interface ConfigurationDataManager {

  fun downloadManifestConfiguration(disableConsentCheck:Boolean,handler : ApiDataProvider<ManifestConfigurationResponse?>)
  fun publishEvents(events: EdgetagMetaData, handler: ApiDataProvider<Any?>)
  fun publishConsentEvents(events: EdgetagMetaData, handler: ApiDataProvider<Any?>)
}
