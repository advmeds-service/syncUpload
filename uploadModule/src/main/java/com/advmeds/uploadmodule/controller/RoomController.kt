package com.advmeds.uploadmodule.controller

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.advmeds.uploadmodule.dao.RequestInfoDao
import com.advmeds.uploadmodule.model.HttpFormat
import com.advmeds.uploadmodule.model.RequestInfo
import com.advmeds.uploadmodule.net.HttpFormatSerialize
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

    fun saveRequestInfo(httpFormat: HttpFormat) {

        sqlQueue.execute {
            val parseController = HttpFormatSerialize()
            val requestInfo = RequestInfo(
                url = parseController.serializationUrl(httpFormat),
                headers = parseController.serializationHeader(httpFormat),
                body = parseController.serializationBody(httpFormat),
                property = parseController.serializationProperty(httpFormat),
                type = httpFormat.requestType,
                uploadState = RequestInfo.UPLOADING
            )
            requestInfoDao.insert(requestInfo)
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