package com.advmeds.uploadmodule.controller

import android.app.Application
import com.advmeds.uploadmodule.model.HttpFormat
import com.advmeds.uploadmodule.model.HttpResponse
import com.advmeds.uploadmodule.model.RequestInfo
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

    fun init(application: Application) {
        this.application = application
    }

    fun request(
        httpFormat: HttpFormat,
        success: ((response: HttpResponse) -> Unit)? = null,
        fail: ((response: HttpResponse) -> Unit)? = null
    ) {
        RoomController.getInstance(application.applicationContext).saveRequestInfo(httpFormat)

        // TODO 请求http
    }

}