package com.advmeds.uploadmodule.controller

import com.advmeds.uploadmodule.model.HttpFormat
import com.advmeds.uploadmodule.utils.EncryptUtil
import com.google.gson.Gson

class HttpFormatParseController {

    fun serializationHeader(httpFormat: HttpFormat): String {
        val header = httpFormat.headers ?: return ""
        if (header.isEmpty()) {
            return ""
        }
        return EncryptUtil.encryptUploadContent(Gson().toJson(header).toByteArray()) ?: ""
    }

    fun serializationBody(httpFormat: HttpFormat): String {
        val body = httpFormat.body ?: return ""
        return EncryptUtil.encryptUploadContent(body) ?: ""
    }

    fun serializationProperty(httpFormat: HttpFormat): String {
        val property = httpFormat.propertyMap ?: return ""
        if (property.isEmpty()) {
            return ""
        }
        return EncryptUtil.encryptUploadContent(Gson().toJson(property).toByteArray()) ?: ""
    }

    fun serializationUrl(httpFormat: HttpFormat): String {
        return EncryptUtil.encryptUploadContent(httpFormat.baseUrl.toByteArray()) ?: ""
    }

    // TODO 反序列化
}