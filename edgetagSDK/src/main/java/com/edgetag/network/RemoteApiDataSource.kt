package com.edgetag.network

import com.edgetag.model.edgetag.EdgetagMetaData
import com.edgetag.model.edgetag.ManifestConfigurationResponse
import retrofit2.Call

class RemoteApiDataSource(private val remoteApiService: RemoteApiService) {

  fun getSDKManifest(disableConsentCheck:Boolean): Call<ManifestConfigurationResponse> {
    return remoteApiService.getSDKManifest(disableConsentCheck)
  }

  fun postEvents(events: EdgetagMetaData): Call<Any> {
    return remoteApiService.postEvents(events)
  }

  fun postConsentEvents(events: EdgetagMetaData): Call<Any> {
    return remoteApiService.postConsentEvents(events)
  }

  fun postUserEvents(events: EdgetagMetaData): Call<Any> {
    return remoteApiService.postUserEvents(events)
  }

  fun postDataEvents(events: EdgetagMetaData): Call<Any> {
    return remoteApiService.postData(events)
  }

  fun getDataEvents(keys:String): Call<Any> {
    return remoteApiService.getData(keys)
  }

  fun getKeys(): Call<Any> {
    return remoteApiService.getKeys()
  }


}