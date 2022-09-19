package com.edgetag.model.edgetag

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Capture {

    @SerializedName("type")
    @Expose
     var type: String? = null

    @SerializedName("key")
    @Expose
     var key: String? = null

    @SerializedName("persist")
    @Expose
     var persist: String? = null


}
