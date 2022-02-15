package com.advmeds.uploadmodule

import android.app.Application
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.advmeds.uploadmodule.controller.RemoteController
import com.advmeds.uploadmodule.model.HttpFormat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RemoteControllerTest {

    @Before
    fun init() {
        RemoteController.init(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application)
    }

    @Test
    fun requestGetTest() {
        val httpFormat = HttpFormat(
            requestType = HttpFormat.GET,
            baseUrl = "https://www.google.com"
        )
        httpFormat.headers = mutableMapOf(
            "title0" to "value0",
            "title1" to "value1",
            "title2" to "value2"
        )
        httpFormat.body = "body test".toByteArray()

        RemoteController.upload(httpFormat)
    }
}