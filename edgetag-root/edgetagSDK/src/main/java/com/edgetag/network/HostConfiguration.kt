package com.edgetag.network

data class HostConfiguration(
        val mConnectionTimeout :Long = 5*60*1000,
        val mReadTimeout : Long = 5*60*1000,
        val mWriteTimeout : Long = 5*60*1000,
        val baseUrl:String?
)
