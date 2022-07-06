package com.edgetag.network

import com.edgetag.model.edgetag.EdgetagMetaData
import com.edgetag.model.edgetag.ManifestConfigurationResponse
import com.edgetag.util.Constant
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RemoteApiService {

  @Retry(3)
  @GET(Constant.EDGE_TAG_REST_API_MANIFEST_PULL_PATH)
  fun getSDKManifest(@Query("consentDisabled") CONSENT_DISABLED_VALUE:Boolean ): Call<ManifestConfigurationResponse>
  @Retry(3)
  @POST(Constant.EDGE_TAG_REST_API_EVENTS_PUSH_PATH)
  fun postEvents(@Body body: EdgetagMetaData): Call<Any>

  @Retry(3)
  @POST(Constant.EDGE_CONSENT_REST_API_EVENTS_PUSH_PATH)
  fun postConsentEvents(@Body body: EdgetagMetaData): Call<Any>

  @Retry(3)
  @POST(Constant.EDGE_USER_EVENTS_PUSH_PATH)
  fun postUserEvents(@Body body: EdgetagMetaData): Call<Any>

  @Retry(3)
  @POST(Constant.EDGE_EVENTS_DATA)
  fun postData(@Body body: EdgetagMetaData): Call<Any>

  @Retry(3)
  @GET(Constant.EDGE_EVENTS_DATA)
  fun getData(@Query("keys") keys:String): Call<Any>

  @Retry(3)
  @GET(Constant.EDGE_EVENTS_GET_KEY)
  fun getKeys(): Call<Any>


}
