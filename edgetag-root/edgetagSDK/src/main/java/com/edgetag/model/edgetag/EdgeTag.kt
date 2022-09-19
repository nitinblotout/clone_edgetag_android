package com.edgetag.model.edgetag

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EdgeTag {

    @SerializedName("providers")
    @Expose
    var providers: List<String>? = null

    @SerializedName("consent")
    @Expose
    var consent: HashMap<String,Boolean>? = null
}
