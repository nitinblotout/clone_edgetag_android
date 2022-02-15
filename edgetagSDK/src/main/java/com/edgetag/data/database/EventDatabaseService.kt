package com.edgetag.data.database

import android.util.Log
import com.edgetag.DependencyInjectorImpl
import com.edgetag.data.database.entity.EventEntity
import com.edgetag.model.edgetag.EdgetagMetaData
import com.edgetag.network.ApiDataProvider
import com.edgetag.repository.EventRepository
import com.edgetag.util.Constant
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call

class EventDatabaseService {

    private var evenDao = DependencyInjectorImpl.getInstance().getEventDatabase().eventDao()
    private var eventRepository: EventRepository = DependencyInjectorImpl.getEventRepository()

    companion object {
        private const val TAG = "EventDatabaseService"
    }

    fun insertEvent(event: EventEntity) {

        CoroutineScope(Dispatchers.Default).launch {
            try {
                DependencyInjectorImpl.getInstance().getEventDatabase().eventDao()
                        .insertEvent(event)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }

    fun getEvents() {

        CoroutineScope(Dispatchers.Default).launch {

            val idTable = ArrayList<Int>()
            evenDao.getEvents().collect { data ->
                data.forEach {
                    try {
                        if (!idTable.contains(it.id)) {
                            idTable.add(it.id)
                            val eventData = it.eventData
                            Log.d("###Pushing id ", "" + it.id)
                            Log.d("###Pushing events ", "" + eventData)

                            val eventObject: EdgetagMetaData = Gson().fromJson(eventData.trim(), EdgetagMetaData::class.java)

                            DependencyInjectorImpl.getInstance().getConfigurationManager()
                                    .publishEvents(eventObject, object : ApiDataProvider<Any?>() {
                                        override fun onFailed(
                                                errorCode: Int,
                                                message: String,
                                                call: Call<Any?>
                                        ) {
                                        }

                                        override fun onError(t: Throwable, call: Call<Any?>) {
                                        }

                                        override fun onSuccess(data: Any?) {
                                            CoroutineScope(Dispatchers.Default).launch {
                                                //Log.d("###deleting id ", "" + it.id)
                                                evenDao.deleteEvent(it)
                                            }
                                        }
                                    })
                        }
                    } catch (e: Exception) {
                        when (e) {
                            is JsonSyntaxException, is IllegalStateException -> {

                                evenDao.deleteEvent(it)
                            }
                        }
                    }
                }
            }
        }
    }
}
