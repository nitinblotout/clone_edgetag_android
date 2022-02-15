package com.edgetag.util

import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.util.*
import kotlin.collections.HashMap


fun Long.sizeFormatter(): String {
    var size = this
        try {
            var suffix: String? = null
            if (size >= 1024) {
                suffix = "KB"
                size /= 1024
                if (size >= 1024) {
                    suffix = "MB"
                    size /= 1024
                        if (size >= 1024) {
                            suffix = "GB"
                            size /= 1024
                             }
                    }
            }
            val stringBuilder = StringBuilder(java.lang.Long.toString(size))
            if (suffix != null) stringBuilder.append(" ").append(suffix)
            return stringBuilder.toString()
        } catch (e: Exception) {
            Log.e("", e.toString())
        }
     return java.lang.Long.toString(size)
    }



fun Context.getVersion():String?{
    return this.packageManager?.getPackageInfo(this.packageName, PackageManager.GET_META_DATA)?.versionName
}

fun stringToIntSum(eventName: String): Int  {
    var eventNameL = eventName.lowercase(Locale.getDefault())
    var sha1String = EncryptionUtils().sha1(eventNameL)
    var sum = 0
    for (index in 0 until sha1String!!.length) {
        val c: Char = sha1String.get(index)
        sum += c.code
    }
    return sum.toInt()

}
