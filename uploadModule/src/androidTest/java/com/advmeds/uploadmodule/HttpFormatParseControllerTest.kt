package com.advmeds.uploadmodule

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.advmeds.uploadmodule.controller.HttpFormatParseController
import com.advmeds.uploadmodule.model.HttpFormat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HttpFormatParseControllerTest {

    private val controller = HttpFormatParseController()

    @Test
    fun parseHeader() {
        val httpFormat = HttpFormat(HttpFormat.GET, "https://www.google.com")
        httpFormat.headers = mutableMapOf(
            "testTitle0" to "test Value0",
            "testTitle1" to  "testValue1"
        )
        val content = controller.serializationHeader(httpFormat)
    }
}