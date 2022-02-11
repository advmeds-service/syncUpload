package com.advmeds.uploadmodule.net

import com.advmeds.uploadmodule.model.HttpFormat
import com.advmeds.uploadmodule.model.HttpResponse
import com.advmeds.uploadmodule.utils.LogUtils
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class Connection {

    private var urlConnection: HttpsURLConnection? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private var httpFormat: HttpFormat? = null

    private val byteArrayOutputStream = ByteArrayOutputStream()

    fun connect(httpFormat: HttpFormat): HttpResponse {
        this.httpFormat = httpFormat

        try {
            val url = URL(httpFormat.baseUrl)
            urlConnection = url.openConnection() as? HttpsURLConnection
            setConfig()
            setHeader()
            connect()
            write()
            read()
        } catch (e: Exception) {
            LogUtils.e(e)
        } finally {

            kotlin.runCatching {
                inputStream?.close()
            }.onFailure {
                inputStream = null
                LogUtils.d("close input error")
            }

            kotlin.runCatching {
                outputStream?.close()
            }.onFailure {
                outputStream = null
                LogUtils.d("close output error")
            }

            urlConnection?.disconnect()
        }

        return getResponse()
    }

    private fun setConfig() {
        urlConnection?.connectTimeout = NetConfig.CONNECT_TIME_OUT
        urlConnection?.readTimeout = NetConfig.READ_TIME_OUT
        urlConnection?.requestMethod = httpFormat?.requestType
        urlConnection?.doOutput = true
        urlConnection?.doInput = true
    }

    private fun setHeader() {
        httpFormat?.headers?.forEach {
             urlConnection?.setRequestProperty(it.key, it.value)
        }
    }

    private fun write() {
        httpFormat?.body?.let {
            outputStream = urlConnection?.outputStream
            outputStream?.write(it)
            outputStream?.flush()
        }
    }

    private fun connect() {
        urlConnection?.connect()
    }

    private fun read(): ByteArray? {
        inputStream = urlConnection?.inputStream ?: return null
        inputStream!!.use {
            it.copyTo(byteArrayOutputStream)
        }
        return byteArrayOutputStream.toByteArray()
    }

    fun getDataString(): String {
        if (byteArrayOutputStream.size() == 0) {
            return ""
        }
        return byteArrayOutputStream.toString()
    }

    fun getDataByteArray(): ByteArray? {
        if (byteArrayOutputStream.size() == 0) {
            return null
        }
        return byteArrayOutputStream.toByteArray()
    }

    private fun getResponse(): HttpResponse {
        val result = HttpResponse()
        kotlin.runCatching {
            result.code = urlConnection?.responseCode ?: -1
            result.content = getDataByteArray()
        }
        return result
    }
}