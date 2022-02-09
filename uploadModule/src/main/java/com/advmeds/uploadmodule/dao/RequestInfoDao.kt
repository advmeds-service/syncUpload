package com.advmeds.uploadmodule.dao

import androidx.room.Dao
import androidx.room.Insert
import com.advmeds.uploadmodule.model.RequestInfo

@Dao
interface RequestInfoDao {

    @Insert
    fun insert(requestInfo: RequestInfo)
}