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
        extraSuccessCodes: IntArray? = null,/* other response code than 200*/
        callback: ((response: HttpResponse) -> Unit)? = null,
    ) {
        RoomController.getInstance(application.applicationContext).saveRequestInfo(httpFormat)

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
            // TODO update db state or delete db request info
        }
    }

    fun executeOnMainThread(func: () -> Unit) {
        handler.post {
            func()
        }
    }

}