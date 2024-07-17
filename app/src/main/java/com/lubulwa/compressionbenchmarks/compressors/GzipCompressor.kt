package com.lubulwa.compressionbenchmarks.compressors

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

class GzipCompressor : Compressor {

    override fun compress(input: String): ByteArray {
        val outputStream = ByteArrayOutputStream()
        GZIPOutputStream(outputStream).use { gzipOutputStream ->
            gzipOutputStream.write(input.toByteArray())
        }
        return outputStream.toByteArray()
    }

    override fun decompress(compressedData: ByteArray): String {
        val inputStream = ByteArrayInputStream(compressedData)
        GZIPInputStream(inputStream).use { gzipInputStream ->
            return gzipInputStream.bufferedReader().use { it.readText() }
        }
    }
}