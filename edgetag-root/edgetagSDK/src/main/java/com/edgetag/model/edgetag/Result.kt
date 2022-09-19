package com.edgetag.model.edgetag

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result {
    @SerializedName("package")
    @Expose
     var _package: String? = null

    @SerializedName("rules")
    @Expose
     var rules: Rules? = null


}
