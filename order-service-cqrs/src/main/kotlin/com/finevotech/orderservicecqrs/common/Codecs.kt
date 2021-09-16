package com.finevotech.orderservicecqrs.common

import org.apache.commons.codec.binary.Base64
import java.nio.ByteBuffer
import java.util.*

class Codecs {
    companion object{
         fun encodeToBase64(str: String): String {
            val base64 = Base64()
            val uuid = UUID.fromString(str)
            val bb = ByteBuffer.wrap(ByteArray(16))
            bb.putLong(uuid.mostSignificantBits)
            bb.putLong(uuid.leastSignificantBits)
            return Base64.encodeBase64URLSafeString(bb.array())
        }

         fun decodeFromBase64(str: String): String {
            val base64 = Base64()
            val bytes: ByteArray = base64.decode(str)
            val bb: ByteBuffer = ByteBuffer.wrap(bytes)
            val uuid = UUID(bb.getLong(), bb.getLong())
            return uuid.toString()
        }
    }
}