package com.advmeds.uploadmodule.controller

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.advmeds.uploadmodule.dao.RequestInfoDao
import com.advmeds.uploadmodule.model.HttpFormat
import com.advmeds.uploadmodule.model.RequestInfo
import com.advmeds.uploadmodule.net.Dispatch
import com.advmeds.uploadmodule.net.HttpFormatSerialization
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class RoomController private constructor(private var applicationContext: Context) {

    private val db = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "upload_db"
    ).build()

    private val sqlQueue = ThreadPoolExecutor(
        1,
        1,
        60,
        TimeUnit.SECONDS,
        LinkedBlockingDeque<Runnable>()
    )

    private val requestInfoDao: RequestInfoDao by lazy {
        db.requestInfoDao()
    }

    fun saveRequestInfo(httpFormat: HttpFormat, time: Long) {
        sqlQueue.execute {
            val serialize = HttpFormatSerialization()
            val requestInfo = serialize.serializationHttpFormat(httpFormat)
            requestInfo.time = time
            requestInfoDao.insert(requestInfo)
        }
    }

    fun getRequestInfo(
        startOffset: Int,
        defaultCount: Int,
        callback: (result: MutableList<RequestInfo>) -> Unit) {
         sqlQueue.execute {
             callback(requestInfoDao.getRequestInfoByState(RequestInfo.UPLOAD_FAIL, startOffset, defaultCount))
         }
    }

    fun getUploadFailCount(result: (count: Int) -> Unit) {
        sqlQueue.execute {
            result(requestInfoDao.getCountByState(RequestInfo.UPLOAD_FAIL))
        }
    }

    fun deleteRequestInfo(time: Long) {
        sqlQueue.execute {
            requestInfoDao.deleteByTime(time)
        }
    }

    fun updateState(time: Long, state: Int) {
        sqlQueue.execute {
            requestInfoDao.updateStateByTime(time, state)
        }
    }

    companion object {
        private var instance: RoomController? = null

        fun getInstance(applicationContext: Context): RoomController {
            synchronized(RoomController::class) {
                if (instance == null) {
                    instance = RoomController(applicationContext)
                }
            }
            return instance!!
        }
    }
}

@Database(
    entities = [RequestInfo::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun requestInfoDao(): RequestInfoDao
}