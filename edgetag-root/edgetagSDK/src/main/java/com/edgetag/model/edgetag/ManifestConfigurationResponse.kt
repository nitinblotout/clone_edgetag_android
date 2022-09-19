package com.edgetag.model.edgetag

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ManifestConfigurationResponse {
    @SerializedName("result")
    @Expose
    var result: List<Result?>? = null

}
