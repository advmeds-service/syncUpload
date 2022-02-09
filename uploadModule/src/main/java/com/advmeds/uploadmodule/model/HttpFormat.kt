package com.advmeds.uploadmodule.model

class HttpFormat(val requestType: String, val baseUrl: String) {
    var headers: MutableMap<String, String>? = null
    var propertyMap: MutableMap<String, String>? = null
    var body: ByteArray? = null

    companion object {
        const val GET = "GET"
        const val POST = "POST"
    }
}

class HttpResponse {
    var code: Int = 0
    var content: ByteArray? = null
}

