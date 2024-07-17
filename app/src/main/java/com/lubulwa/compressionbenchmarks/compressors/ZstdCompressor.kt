package com.lubulwa.compressionbenchmarks.compressors

import com.github.luben.zstd.Zstd
import java.nio.charset.Charset

class ZstdCompressor : Compressor {

    override fun compress(input: String): ByteArray {
        return try {
            Zstd.compress(input.toByteArray())
        } catch (exception: IllegalStateException) {
            ByteArray(0)
        }
    }

    override fun decompress(compressedData: ByteArray): String {
        return try {
            val result = Zstd.decompress(compressedData, Zstd.getFrameContentSize(compressedData).toInt())
            String(result, Charset.defaultCharset())
        } catch (exception: IllegalStateException) {
            ""
        }
    }
}