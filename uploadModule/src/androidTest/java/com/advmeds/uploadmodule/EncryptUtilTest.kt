package com.advmeds.uploadmodule

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.advmeds.uploadmodule.utils.EncryptUtil
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EncryptUtilTest {

    @Test
    fun getAesKeyTest() {
        EncryptUtil.getAesKey()?.encoded
    }

    @Test
    fun encryptAndDecryptTest() {
        val content = "djsklj123io9sfjlk@3lksj"
        val encryptContent = EncryptUtil.encryptUploadContent(content.toByteArray())
        assert(encryptContent != null)
        val decryptContent = EncryptUtil.decryptUploadContent(encryptContent!!)
        assert(decryptContent != null)
        assert(content == String(decryptContent!!))
    }
}