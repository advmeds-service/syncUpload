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

    @Query("SELECT * FROM RequestInfo WHERE uploadState = :state LIMIT :startOffset, :defaultCount")
    fun getRequestInfoByState(state: Int, startOffset: Int, defaultCount: Int): MutableList<RequestInfo>

    @Query("SELECT COUNT(*) FROM RequestInfo WHERE uploadState = :state")
    fun getCountByState(state: Int): Int
}