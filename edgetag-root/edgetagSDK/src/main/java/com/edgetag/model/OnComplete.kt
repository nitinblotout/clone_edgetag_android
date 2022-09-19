package com.edgetag.model

interface OnComplete {
   fun onSuccess(msg : Any)
   fun onError(code : Int = ErrorCodes.ERROR_CODE_NO_ERROR, msg : String = "")
}
