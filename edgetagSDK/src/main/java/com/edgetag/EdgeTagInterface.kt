package com.edgetag

import android.app.Application
import com.edgetag.model.CompletionHandler

interface EdgeTagInterface {

    fun init(application: Application, edgeTagConfiguration: EdgeTagConfiguration, completionHandler: CompletionHandler)

    fun consent( consentInfo:HashMap<String, Boolean>,completionHandler: CompletionHandler)

    fun tag( eventName:String,tagInfo:HashMap<String, Any>?,providerInfo:HashMap<String, Boolean>?,completionHandler: CompletionHandler)

    fun user(
        key: String,
        value: String,
        completionHandler: CompletionHandler
    )
}
