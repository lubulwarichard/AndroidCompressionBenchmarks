package com.lubulwa.compressionbenchmarks.utils

import com.lubulwa.compressionbenchmarks.compressors.GzipCompressor
import com.lubulwa.compressionbenchmarks.compressors.SnappyCompressor
import com.lubulwa.compressionbenchmarks.compressors.ZstdCompressor
import com.lubulwa.compressionbenchmarks.ui.AlgorithmType
import javax.inject.Inject

class CompressionHelper @Inject constructor() {

    fun compress(name: AlgorithmType, inputString: String): ByteArray {
        when(name) {
            AlgorithmType.GZIP -> {
                val compressor = GzipCompressor()
                return compressor.compress(inputString)
            }
            AlgorithmType.SNAPPY -> {
                val compressor = SnappyCompressor()
                return compressor.compress(inputString)
            }
            AlgorithmType.ZSTD -> {
                val compressor = ZstdCompressor()
                return compressor.compress(inputString)
            }
        }
    }

    fun decompress(name: AlgorithmType, compressedData: ByteArray): String {
        when(name) {
            AlgorithmType.GZIP -> {
                val compressor = GzipCompressor()
                return compressor.decompress(compressedData)
            }
            AlgorithmType.SNAPPY -> {
                val compressor = SnappyCompressor()
                return compressor.decompress(compressedData)
            }
            AlgorithmType.ZSTD -> {
                val compressor = ZstdCompressor()
                return compressor.decompress(compressedData)
            }
        }
    }
}