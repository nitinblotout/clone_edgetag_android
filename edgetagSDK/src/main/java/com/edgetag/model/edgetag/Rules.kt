package com.edgetag.model.edgetag

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Rules {

    @SerializedName("capture")
    @Expose
    var capture: List<Capture>? = null
}
