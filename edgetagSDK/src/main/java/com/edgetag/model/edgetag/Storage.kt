package com.edgetag.model.edgetag

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Storage {
  @SerializedName("edgeTag")
  @Expose
  var edgeTag: EdgeTag? = null

  @SerializedName("data")
  @Expose
  var data: HashMap<String, HashMap<String, String>>? = null

}
