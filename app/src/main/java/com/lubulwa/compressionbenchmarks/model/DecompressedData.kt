package com.lubulwa.compressionbenchmarks.model

data class DecompressedData(
    val algorithm: String,
    val pageName: String,
    val timeTaken: List<Long>,
    val data: String,
    val sizeInBytes: Double
)