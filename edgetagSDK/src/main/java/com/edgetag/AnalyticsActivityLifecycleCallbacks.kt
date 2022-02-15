package com.edgetag

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import com.edgetag.repository.EventRepository
import com.edgetag.repository.data.SharedPreferenceSecureVault
import com.edgetag.util.Constant
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.lang.Exception
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger


class AnalyticsActivityLifecycleCallbacks(
        private var eventRepository: EventRepository,
        private var secureStorage: SharedPreferenceSecureVault
) :
        Application.ActivityLifecycleCallbacks,
        ComponentCallbacks2,
        DefaultLifecycleObserver {
    private var numberOfActivities: AtomicInteger? = null
    private var trackedApplicationLifecycleEvents: AtomicBoolean? = null
    private var firstLaunch: AtomicBoolean? = null
    private var activityReference: WeakReference<Activity>? = null


    companion object {
        private const val TAG = "LifecycleCallbacks"
    }

    init {
        try {
            numberOfActivities = AtomicInteger(1)
            firstLaunch = AtomicBoolean(false)
            trackedApplicationLifecycleEvents = AtomicBoolean(false)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        try {
            if (!trackedApplicationLifecycleEvents!!.getAndSet(true)) {
                numberOfActivities!!.set(0)
                firstLaunch!!.set(true)
                trackApplicationLifecycleEvents(activity)
                trackDeepLink(activity)
                DependencyInjectorImpl.getEventRepository().visibleActivity = activity
            }
            activityReference = WeakReference(activity)
            val view = activity.window.decorView.rootView
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }


    private fun getPackageInfo(context: Context): PackageInfo? {
        val packageManager = context.packageManager
        return try {
            packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, e.toString())
            null
        }
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        //eventRepository.prepareSystemEvent(activity, Constant.BO_VISIBILITY_HIDDEN, null, Constant.BO_EVENT_VISIBILITY_HIDDEN)

    }

    override fun onConfigurationChanged(newConfig: Configuration) {

    }

    override fun onLowMemory() {

    }

    override fun onTrimMemory(level: Int) {

    }


    private fun trackApplicationLifecycleEvents(activity: Activity) {
    }

    private fun trackDeepLink(activity: Activity) {
        try {
            val intent = activity.intent
            if (intent == null || intent.data == null) {
                return
            }
            val properties = hashMapOf<String, Any>()
            var uri = intent.data
             uri = Uri.parse("https://sdk-demo.edgetag.io/?fbclid=12345")
            for (parameter in uri!!.queryParameterNames) {
                val value = uri.getQueryParameter(parameter)
                if (value != null && !value.trim { it <= ' ' }.isEmpty()) {
                    properties[parameter] = value
                }
            }

            DependencyInjectorImpl.getInstance().getSecureStorageService().storeString(Constant.REFFERAL,Gson().toJson(properties))
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }


}
