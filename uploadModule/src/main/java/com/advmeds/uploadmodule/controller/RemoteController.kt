package com.advmeds.uploadmodule.controller

import android.app.Application
import android.os.Handler
import android.os.Looper
import com.advmeds.uploadmodule.model.HttpFormat
import com.advmeds.uploadmodule.model.HttpResponse
import com.advmeds.uploadmodule.model.RequestInfo
import com.advmeds.uploadmodule.net.Connection
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

    private val handler = Handler(Looper.getMainLooper())

    lateinit var application: Application
        private set

    fun init(application: Application) {
        this.application = application
    }

    fun request(
        httpFormat: HttpFormat,
        isAsynchronous: Boolean = true,
        extraSuccessCodes: IntArray? = null,/* 除200 以外 逻辑上认为成功的响应码*/
        callback: ((response: HttpResponse) -> Unit)? = null
    ) {
        val time = System.currentTimeMillis()
        RoomController.getInstance(application.applicationContext).saveRequestInfo(httpFormat, time)

        remoteQueue.execute {
            val connection = Connection()
            val response = connection.connect(httpFormat)
            callback?.let {
                if (isAsynchronous) {
                    executeOnMainThread {
                        it(response)
                    }
                } else {
                    it(response)
                }
            }

            if (isSuccess(response, extraSuccessCodes)) {
                deleteRequestInfo(time)
            } else {
                uploadFail(time)
            }
        }
    }

    private fun executeOnMainThread(func: () -> Unit) {
        handler.post {
            func()
        }
    }

    private fun deleteRequestInfo(time: Long) {
        executeOnMainThread {
            RoomController.getInstance(application.applicationContext).deleteRequestInfo(time)
        }
    }

    private fun uploadFail(time: Long) {
        executeOnMainThread {
            RoomController.getInstance(application.applicationContext).updateState(time, RequestInfo.UPLOAD_FAIL)
        }
    }

    private fun isSuccess(
        response: HttpResponse,
        extraSuccessCodes: IntArray?): Boolean {
        return response.code == 200 || extraSuccessCodes?.contains(response.code) == true
    }

}