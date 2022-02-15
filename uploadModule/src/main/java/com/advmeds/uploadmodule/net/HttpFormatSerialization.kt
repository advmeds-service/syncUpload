package com.advmeds.uploadmodule.net

import com.advmeds.uploadmodule.model.HttpFormat
import com.advmeds.uploadmodule.model.RequestInfo
import com.advmeds.uploadmodule.utils.EncryptUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HttpFormatSerialization : Serialization() {

    fun serializationHttpFormat(httpFormat: HttpFormat): RequestInfo {
        return RequestInfo(
            url = serializationString(httpFormat.baseUrl),
            headers = serializationMap(httpFormat.headers),
            body = serializationByteArray(httpFormat.body),
            type = serializationString(httpFormat.requestType),
            extraSuccessCodes = serializeIntArray(httpFormat.extraSuccessCodes),
            uploadState = RequestInfo.UPLOADING
        )
    }

    fun deserializeHttpFormat(requestInfo: RequestInfo): HttpFormat {
        val result = HttpFormat(
            requestType = deserializeString(requestInfo.type),
            baseUrl = deserializeString(requestInfo.url)
        )
        result.headers = deserializeMap(requestInfo.headers)
        result.body = deserializeByteArray(requestInfo.body)
        result.extraSuccessCodes = deserializeIntArray(requestInfo.extraSuccessCodes)
        return result
    }
}