package com.lubulwa.compressionbenchmarks.compressors

import com.jiechic.library.android.snappy.Snappy


class SnappyCompressor : Compressor {

    override fun compress(input: String): ByteArray {
        return try {
            Snappy.compress(input)
        } catch (exception: Exception) {
            ByteArray(0)
        }
    }

    override fun decompress(compressedData: ByteArray): String {
        return try {
            Snappy.uncompressString(compressedData)
        } catch (exception: Exception) {
            ""
        }
    }
}