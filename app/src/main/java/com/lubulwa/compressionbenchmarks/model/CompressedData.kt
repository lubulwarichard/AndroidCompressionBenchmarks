package com.lubulwa.compressionbenchmarks.model

data class CompressedData(
    val algorithm: String,
    val pageName: String,
    val data: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CompressedData

        if (algorithm != other.algorithm) return false
        if (pageName != other.pageName) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = algorithm.hashCode()
        result = 31 * result + pageName.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}