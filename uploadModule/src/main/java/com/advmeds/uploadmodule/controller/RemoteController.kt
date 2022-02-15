package com.advmeds.uploadmodule.controller

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import com.advmeds.uploadmodule.model.HttpFormat
import com.advmeds.uploadmodule.model.HttpResponse
import com.advmeds.uploadmodule.model.RequestInfo
import com.advmeds.uploadmodule.net.Connection
import com.advmeds.uploadmodule.net.Dispatch
import com.advmeds.uploadmodule.net.HttpFormatSerialization
import com.advmeds.uploadmodule.utils.LogUtils
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object RemoteController {

    private val remoteQueue = ThreadPoolExecutor(
        1,
        1,
        60,
        TimeUnit.SECONDS,
        LinkedBlockingDeque<Runnable>()
    )

    lateinit var application: Application
        private set

    @MainThread
    fun init(application: Application) {
        this.application = application
    }

    @MainThread
    fun upload(
        httpFormat: HttpFormat,
        callbackOnMainThread: Boolean = true,
        callback: ((response: HttpResponse) -> Unit)? = null
    ) {
        val time = System.currentTimeMillis()
        RoomController.getInstance(application.applicationContext).saveRequestInfo(httpFormat, time)

        startConnect(httpFormat, callback, callbackOnMainThread, time)
    }

    @MainThread
    fun reUpload(callbackOnMainThread: Boolean = true, callback: ((response: HttpResponse) -> Unit)? = null) {
        RoomController.getInstance(application.applicationContext).getUploadFailCount {
            if (it > 0) {
                restartConnect(it, callbackOnMainThread, callback)
            }
        }
    }

    private fun restartConnect(count: Int, callbackOnMainThread: Boolean = true, callback: ((response: HttpResponse) -> Unit)? = null) {
        val defaultCount = 1
        val serialize = HttpFormatSerialization()
        RoomController.getInstance(application.applicationContext).getRequestInfo(0, defaultCount) {

            Dispatch.executeOnMainThread {
                for (item in it) {
                    RoomController.getInstance(application.applicationContext).updateState(item.time, RequestInfo.UPLOADING)
                    LogUtils.d("restart ${item.id}")
                    startConnect(
                        httpFormat = serialize.deserializeHttpFormat(item),
                        callback = callback,
                        callbackOnMainThread = callbackOnMainThread,
                        item.time
                    )
                }

                if (count > 0 && it.size == defaultCount) {
                    restartConnect(count - defaultCount)
                } else {
                    LogUtils.d("end restartConnect")
                }
            }

        }
    }

    private fun startConnect(
        httpFormat: HttpFormat,
        callback: ((response: HttpResponse) -> Unit)?,
        callbackOnMainThread: Boolean,
        time: Long
    ) {
        remoteQueue.execute {
            val connection = Connection()
            val response = connection.connect(httpFormat)
            callback?.let {
                if (callbackOnMainThread) {
                    Dispatch.executeOnMainThread {
                        it(response)
                    }
                } else {
                    it(response)
                }
            }

            if (isSuccess(response, httpFormat.extraSuccessCodes)) {
                deleteRequestInfo(time)
            } else {
                uploadFail(time)
            }
        }
    }

    private fun deleteRequestInfo(time: Long) {
        Dispatch.executeOnMainThread {
            RoomController.getInstance(application.applicationContext).deleteRequestInfo(time)
        }
    }

    private fun uploadFail(time: Long) {
        Dispatch.executeOnMainThread {
            RoomController.getInstance(application.applicationContext).updateState(time, RequestInfo.UPLOAD_FAIL)
        }
    }

    private fun isSuccess(
        response: HttpResponse,
        extraSuccessCodes: IntArray?): Boolean {
        return response.code == 200 || extraSuccessCodes?.contains(response.code) == true
    }

}