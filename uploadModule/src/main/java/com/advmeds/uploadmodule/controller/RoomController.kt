package com.advmeds.uploadmodule.controller

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

class RoomController private constructor(private var applicationContext: Context){

    private val db = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "upload_db"
    ).build()

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
    entities = arrayOf(),
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
}