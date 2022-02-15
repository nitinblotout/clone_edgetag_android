package com.edgetag

import android.app.Application
import com.edgetag.DependencyInjectorImpl
import com.edgetag.EdgeTag
import com.edgetag.model.CompletionHandler
import com.edgetag.util.Errors
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class EdgeTagPublicApiTest {

    lateinit var context: Application

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    lateinit var  dependencyInjectorImpl:DependencyInjectorImpl


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        context = Mockito.mock(Application::class.java)
        dependencyInjectorImpl = mock(DependencyInjectorImpl::class.java)
    }

    @Test
    fun testKeysForPositive() {
        val blotoutAnalyticsConfiguration = MockTestConstants.setupBlotoutAnalyticsConfiguration()
        val errorCode = blotoutAnalyticsConfiguration.validateRequest()
        Assert.assertEquals(Errors.ERROR_CODE_NO_ERROR, errorCode)
    }

    @Test
    fun testKeysForNegative() {
        val blotoutAnalyticsConfiguration = MockTestConstants.setupBlotoutAnalyticsConfiguration()
        val errorCode = blotoutAnalyticsConfiguration.validateRequest()
        Assert.assertEquals(Errors.ERROR_KEY_NOT_PROPER, errorCode)
    }

    @Test
    fun testUrlsForNegative() {
        val blotoutAnalyticsConfiguration = MockTestConstants.setupBlotoutAnalyticsConfiguration()
        blotoutAnalyticsConfiguration.endPointUrl = ""
        val errorCode = blotoutAnalyticsConfiguration.validateRequest()
        Assert.assertEquals(Errors.ERROR_URL_NOT_PROPER, errorCode)
    }

    @Test
    fun testSDKStart() {
        val edgeTagConfiguration = MockTestConstants.setupBlotoutAnalyticsConfiguration()
        EdgeTag.init(context, edgeTagConfiguration,object : CompletionHandler{
            override fun onSuccess() {
                assertTrue(true)
            }

          override fun onError(code: Int, msg: String) {
            TODO("Not yet implemented")
          }

        })
    }

}
