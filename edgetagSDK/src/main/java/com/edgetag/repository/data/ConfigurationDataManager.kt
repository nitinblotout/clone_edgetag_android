package com.edgetag.repository.data

import com.edgetag.model.Result
import com.edgetag.model.edgetag.EdgetagMetaData
import com.edgetag.model.edgetag.ManifestConfigurationResponse
import com.edgetag.network.ApiDataProvider

interface ConfigurationDataManager {

  suspend fun downloadManifestConfiguration(): Result<ManifestConfigurationResponse?>
  fun publishEvents(events: EdgetagMetaData, handler: ApiDataProvider<Any?>)
  fun publishConsentEvents(events: EdgetagMetaData, handler: ApiDataProvider<Any?>)
}
