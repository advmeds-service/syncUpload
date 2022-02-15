package com.advmeds.uploadmodule.model

class HttpFormat(val requestType: String, val baseUrl: String) {
    var headers: MutableMap<String, String>? = null
    var body: ByteArray? = null

    /**
     * 除200 以外 逻辑上认为成功的响应码
     */
    var extraSuccessCodes: IntArray? = null

    companion object {
        const val GET = "GET"
        const val POST = "POST"
    }
}

class HttpResponse {
    var code: Int = 0
    var content: ByteArray? = null

    fun getString(): String {
        content?.let {
            return String(it)
        }
        return ""
    }
}

