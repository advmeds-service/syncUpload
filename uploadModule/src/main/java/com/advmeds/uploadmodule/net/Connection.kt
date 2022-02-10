package com.advmeds.uploadmodule.net

import android.net.Uri
import com.advmeds.uploadmodule.model.HttpFormat
import com.advmeds.uploadmodule.utils.LogUtils
import java.net.HttpURLConnection
import java.net.URL

class Connection {

    fun connect(httpFormat: HttpFormat) {
        var httpURLConnection: HttpURLConnection? = null
        kotlin.runCatching {
            val url = URL(httpFormat.baseUrl)
            httpURLConnection = url.openConnection() as? HttpURLConnection
            setConfig(httpURLConnection, httpFormat)
            setHeader(httpURLConnection, httpFormat)
            setProperty(httpURLConnection, httpFormat)
            setBody(httpURLConnection, httpFormat)
        }.onSuccess {
            httpURLConnection?.disconnect()
        }.onFailure {
            LogUtils.e(it)
            httpURLConnection?.disconnect()
        }
    }

    private fun setConfig(httpURLConnection: HttpURLConnection?, httpFormat: HttpFormat) {
        httpURLConnection?.connectTimeout = NetConfig.CONNECT_TIME_OUT
        httpURLConnection?.readTimeout = NetConfig.READ_TIME_OUT
        httpURLConnection?.requestMethod = httpFormat.requestType
    }

    private fun setHeader(httpURLConnection: HttpURLConnection?, httpFormat: HttpFormat) {

    }

    private fun setProperty(httpURLConnection: HttpURLConnection?, httpFormat: HttpFormat) {

    }

    private fun setBody(httpURLConnection: HttpURLConnection?, httpFormat: HttpFormat) {

    }
}