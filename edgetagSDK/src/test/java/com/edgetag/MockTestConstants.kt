package com.edgetag

import com.edgetag.EdgeTagConfiguration

object MockTestConstants {

    fun setupBlotoutAnalyticsConfiguration():EdgeTagConfiguration {
        var edgeTagAnalyticsConfiguration = EdgeTagConfiguration()
        edgeTagAnalyticsConfiguration.endPointUrl = "https://stage.blotout.io/sdk/"
        edgeTagAnalyticsConfiguration.disableConsentCheck = true
        return edgeTagAnalyticsConfiguration
    }
}
