package com.edgetag

import android.app.Application
import com.edgetag.model.CompletionHandler
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class ExampleInstrumentedTest {

  lateinit var context: Application

  lateinit var edgeTagConfiguration: EdgeTagConfiguration


  @Before
  fun setUp() {
    MockitoAnnotations.openMocks(this)
    Mockito.`when`(context.applicationContext).thenReturn(context)
    edgeTagConfiguration.endPointUrl = "https://stage.blotout.io/sdk/"
    EdgeTag.init(
      application = context,
      edgeTagConfiguration = edgeTagConfiguration, object : CompletionHandler {
        override fun onSuccess() {
        }

        override fun onError(code: Int, msg: String) {
          TODO("Not yet implemented")
        }


      })
  }

  @Test
  fun testKeys() {
    var errorCode = edgeTagConfiguration.validateRequest()
    Assert.assertEquals(0, errorCode)
  }
}
