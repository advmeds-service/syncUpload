package com.advmeds.uploadmodule.net

import com.advmeds.uploadmodule.model.HttpFormat
import com.advmeds.uploadmodule.utils.EncryptUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HttpFormatSerialize {

    fun serializationMap(value: MutableMap<String, String>?): String {
        if (value == null || value.isEmpty()) return ""
        return EncryptUtil.encryptUploadContent(Gson().toJson(value).toByteArray()) ?: ""
    }

    fun serializationString(value: String?): String {
        if (value == null || value.isEmpty()) {
            return ""
        }
        return EncryptUtil.encryptUploadContent(value.toByteArray()) ?: ""
    }

    fun serializationByteArray(byteArray: ByteArray?): String {
        if (byteArray == null || byteArray.isEmpty()) {
            return ""
        }
        return EncryptUtil.encryptUploadContent(byteArray) ?: ""
    }

    fun deserializeMap(value: String): MutableMap<String, String>? {
        if (value.isEmpty()) {
            return null
        }
        val decryptContent = EncryptUtil.decryptUploadContent(value)
        if (decryptContent == null || decryptContent.isEmpty()) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<MutableMap<String, String>>() {}.type
        return gson.fromJson(String(decryptContent), type)
    }

    fun deserializeString(value: String): String {
        if (value.isEmpty()) {
            return ""
        }
        EncryptUtil.decryptUploadContent(value)?.let {
            return String(it)
        }
        return  ""
    }

    fun deserializeByteArray(value: String?): ByteArray? {
        if (value == null || value.isEmpty()) {
            return null
        }
        return EncryptUtil.decryptUploadContent(value)
    }

}