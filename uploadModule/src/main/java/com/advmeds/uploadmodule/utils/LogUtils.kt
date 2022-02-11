package com.advmeds.uploadmodule.utils

import android.util.Log
import java.lang.Exception

class LogUtils {

    companion object {
        private const val tag = "uploadModule"
        var showLog = true

        fun d(message: String) {
            if (showLog) {
                Log.d(tag, message)
            }
        }

        fun e(tr: Throwable, message: String? = "") {
            if (showLog) {
                Log.e(tag, message, tr)
            }
        }
    }
}