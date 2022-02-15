package com.advmeds.uploadmodule.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RequestInfo(
    val url: String,
    val headers: String,
    val body: String = "",
    val property: String = "",
    val extraSuccessCodes: String = "",
    var time: Long = System.currentTimeMillis(),
    val type: String,
    val uploadState: Int
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    companion object {
        const val UPLOAD_SUCCESS = 1
        const val UPLOAD_FAIL = -1
        const val UPLOADING = 0
    }
}