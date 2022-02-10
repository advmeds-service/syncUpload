package com.advmeds.uploadmodule.utils

import android.annotation.SuppressLint
import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class EncryptUtil {

    companion object {

        val key = byteArrayOf(
            4, -5, -19, 65, 78, -92, 72, 32,
            89, 0, -32, -5, -94, -67, -73, 103,
            37, -69, 58, -107, -51, 3, -102, 25,
            92, -128, -37, 2, -41, -57, -17, 49
        )

        private const val AES_PKCS7Padding = "AES/ECB/PKCS7Padding"

        fun aesEncrypt(aesKey: ByteArray, content: ByteArray): ByteArray? {
            return try {
                val secretKeySpec = SecretKeySpec(aesKey, AES_PKCS7Padding)
                val cipher = Cipher.getInstance(AES_PKCS7Padding)
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
                cipher.doFinal(content)
            } catch (e: Exception) {
                LogUtils.e(e)
                null
            }
        }

        fun aesDecrypt(aesKey: ByteArray, content: ByteArray): ByteArray? {
            return try {
                val secretKeySpec = SecretKeySpec(aesKey, AES_PKCS7Padding)
                val cipher = Cipher.getInstance(AES_PKCS7Padding)
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
                cipher.doFinal(content)
            } catch (e: Exception) {
                LogUtils.e(e)
                null
            }
        }

        fun getAesKey(): SecretKey? {
            try {
                val keyGenerator = KeyGenerator.getInstance("AES")
                val secureRandom = SecureRandom()
                keyGenerator.init(256, secureRandom)
                return keyGenerator.generateKey()
            } catch (e: Exception) {
                LogUtils.e(e)
                return null
            }
        }

        fun base64Encode(content: ByteArray): String {
            return Base64.encodeToString(content, Base64.DEFAULT)
        }

        fun encryptUploadContent(data: ByteArray): String? {
            aesEncrypt(key, data)?.let {
                return base64Encode(it)
            }
            return null
        }

        fun decryptUploadContent(data: String): ByteArray? {
            return aesDecrypt(key, Base64.decode(data, Base64.DEFAULT))
        }
    }
}