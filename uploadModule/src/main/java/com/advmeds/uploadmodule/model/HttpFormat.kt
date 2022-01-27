package com.advmeds.uploadmodule.model

open class HttpFormat {
    var baseUrl: String? = null
    var headers: MutableMap<String, String>? = null
}

class HttpGet(
) : HttpFormat() {

}

class HttpPost(
    var propertyMap: MutableMap<String, String>? = null,
    val data: ByteArray
) : HttpFormat() {

}

