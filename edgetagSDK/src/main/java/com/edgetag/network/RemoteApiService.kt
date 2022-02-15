package com.edgetag.network

import com.edgetag.model.edgetag.EdgetagMetaData
import com.edgetag.model.edgetag.ManifestConfigurationResponse
import com.edgetag.util.Constant
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RemoteApiService {

  @Retry(3)
  @GET(Constant.EDGE_TAG_REST_API_MANIFEST_PULL_PATH)
  suspend fun getSDKManifest(): Response<ManifestConfigurationResponse>

  @Retry(3)
  @POST(Constant.EDGE_TAG_REST_API_EVENTS_PUSH_PATH)
  fun postEvents(@Body body: EdgetagMetaData): Call<Any>

  @Retry(3)
  @POST(Constant.EDGE_CONSENT_REST_API_EVENTS_PUSH_PATH)
  fun postConsentEvents(@Body body: EdgetagMetaData): Call<Any>


}
