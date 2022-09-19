package com.edgetag.model

interface CompletionHandler {
   fun onSuccess()
   fun onError(code : Int = ErrorCodes.ERROR_CODE_NO_ERROR, msg : String = "")
}
