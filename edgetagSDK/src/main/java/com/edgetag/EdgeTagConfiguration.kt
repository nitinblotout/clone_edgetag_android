package com.edgetag

import android.util.Log
import android.webkit.URLUtil
import com.edgetag.util.Constant
import com.edgetag.util.Errors
import java.lang.Exception

class EdgeTagConfiguration {


  var endPointUrl: String? = null
  var disableConsentCheck:Boolean = true

  companion object {
    private const val TAG = "AnalyticsConfiguration"
  }

  fun validateRequest(): Int {
    if (endPointUrl.isNullOrEmpty() || !URLUtil.isValidUrl(endPointUrl))
      return Errors.ERROR_URL_NOT_PROPER
    save()
    return Errors.ERROR_CODE_NO_ERROR
  }

  private fun save() {
    try {
      val storageService = DependencyInjectorImpl.getInstance().getSecureStorageService()
      storageService.storeString(Constant.SDK_END_POINT_URL, endPointUrl!!)
    } catch (e: Exception) {
      Log.e(TAG,e.localizedMessage)
    }
  }
}
