package com.advmeds.uploadmodule

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.advmeds.uploadmodule.net.HttpFormatSerialization
import com.google.gson.Gson
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HttpFormatSerializationTest {

    private val httpFormatSerialize = HttpFormatSerialization()

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

    @Test
    fun serializeByIntArrayTest() {
        val array0 = intArrayOf(1, 2, 3)
        val array1 = intArrayOf()
        val array2: IntArray? = null

        val resultIntArray = httpFormatSerialize.deserializeIntArray(httpFormatSerialize.serializeIntArray(array0))
        assert(resultIntArray!!.isNotEmpty())
        for (i in resultIntArray.indices) {
            assert(resultIntArray[i] == array0[i])
        }

        assert(httpFormatSerialize.deserializeIntArray(httpFormatSerialize.serializeIntArray(array1)) == null)
        assert(httpFormatSerialize.deserializeIntArray(httpFormatSerialize.serializeIntArray(array2)) == null)
    }

    @Test
    fun test() {
        val gson = Gson()
        val array = intArrayOf(2, 3, 4)
        val test = gson.toJson(array)
    }
}