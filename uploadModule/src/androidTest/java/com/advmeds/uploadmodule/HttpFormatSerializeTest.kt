package com.advmeds.uploadmodule

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.advmeds.uploadmodule.net.HttpFormatSerialize
import com.advmeds.uploadmodule.model.HttpFormat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HttpFormatSerializeTest {

    private val httpFormatSerialize = HttpFormatSerialize()

    @Test
    fun parserMapTest() {
        val title0 = "testTitle0"
        val value0 = "test Value0"
        val title1 = "testTitle1"
        val value1 = "testValue1"
        val map0 = mutableMapOf(
            title0 to value0,
            title1 to value1
        )
        val map1 = mutableMapOf<String, String>()
        val map2: MutableMap<String, String>? = null

        val result = httpFormatSerialize.deserializeMap(httpFormatSerialize.serializationMap(map0))
        assert(result!![title0] == value0)
        assert(result[title1] == value1)

        assert(httpFormatSerialize.deserializeMap(httpFormatSerialize.serializationMap(map1)) == null)
        assert(httpFormatSerialize.deserializeMap(httpFormatSerialize.serializationMap(map2)) == null)
    }

    @Test
    fun parserStringTest() {
        val value0 = "value0"
        val value1 = ""
        val value2 = null
        assert(httpFormatSerialize.deserializeString(httpFormatSerialize.serializationString(value0)) == value0)
        assert(httpFormatSerialize.deserializeString(httpFormatSerialize.serializationString(value1)) == value1)
        assert(httpFormatSerialize.deserializeString(httpFormatSerialize.serializationString(value2)) == value1)
    }

    @Test
    fun serializationByteArrayTest() {
        val byteArray0 = "sdfjk12938hkjs8".toByteArray()
        val byteArray1 = byteArrayOf()
        val byteArray2: ByteArray? = null

        val resultByteArray = httpFormatSerialize.deserializeByteArray(httpFormatSerialize.serializationByteArray(byteArray0))
        assert(resultByteArray!!.isNotEmpty())
        for (i in resultByteArray.indices) {
            assert(byteArray0[i] == resultByteArray[i])
        }

        assert(httpFormatSerialize.deserializeByteArray(httpFormatSerialize.serializationByteArray(byteArray1)) == null)
        assert(httpFormatSerialize.deserializeByteArray(httpFormatSerialize.serializationByteArray(byteArray2)) == null)

    }
}