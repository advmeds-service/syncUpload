package com.advmeds.uploadmodule.net

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import com.advmeds.uploadmodule.utils.LogUtils

class Dispatch {
    companion object {

        private val handler = Handler(Looper.getMainLooper())

        fun executeOnMainThread(func: () -> Unit) {
            handler.post {
                func()
            }
        }
    }
}