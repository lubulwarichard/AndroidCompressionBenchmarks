package com.lubulwa.compressionbenchmarks.compressors

interface Compressor {

    fun compress(input: String): ByteArray

    fun decompress(compressedData: ByteArray): String
}