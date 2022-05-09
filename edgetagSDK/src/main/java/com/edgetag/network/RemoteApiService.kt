package com.edgetag.network

import com.edgetag.model.edgetag.EdgetagMetaData
import com.edgetag.model.edgetag.ManifestConfigurationResponse
import com.edgetag.util.Constant
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RemoteApiService {

  @Retry(3)
  @GET(Constant.EDGE_TAG_REST_API_MANIFEST_PULL_PATH)
  fun getSDKManifest(): Call<ManifestConfigurationResponse>

  @Retry(3)
  @POST(Constant.EDGE_TAG_REST_API_EVENTS_PUSH_PATH)
  fun postEvents(@Body body: EdgetagMetaData): Call<Any>

  @Retry(3)
  @POST(Constant.EDGE_CONSENT_REST_API_EVENTS_PUSH_PATH)
  fun postConsentEvents(@Body body: EdgetagMetaData): Call<Any>


}
