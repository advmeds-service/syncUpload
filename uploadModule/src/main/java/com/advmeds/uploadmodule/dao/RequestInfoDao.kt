package com.advmeds.uploadmodule.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.advmeds.uploadmodule.model.RequestInfo

@Dao
interface RequestInfoDao {

    @Insert
    fun insert(requestInfo: RequestInfo)

    @Query("DELETE FROM RequestInfo WHERE time = :time")
    fun deleteByTime(time: Long)

    @Query("UPDATE RequestInfo SET uploadState = :state WHERE time = :time")
    fun updateStateByTime(time: Long, state: Int)
}