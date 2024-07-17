package com.lubulwa.compressionbenchmarks.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lubulwa.compressionbenchmarks.model.BenchMarkData
import com.lubulwa.compressionbenchmarks.model.CompressedData
import com.lubulwa.compressionbenchmarks.model.DecompressedData
import com.lubulwa.compressionbenchmarks.utils.CompressionHelper
import com.lubulwa.compressionbenchmarks.utils.ExcelUtils
import com.lubulwa.compressionbenchmarks.utils.PageHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class BenchMarkViewModel @Inject constructor(
    private val compressionHelper: CompressionHelper,
    private val pageHelper: PageHelper,
    private val excelUtils: ExcelUtils
) : ViewModel() {

    private val _benchMarks = MutableStateFlow(emptyList<BenchMarkData>())
    val benchMarks = _benchMarks

    private val _progressInfo = MutableStateFlow("")
    val progressInfo = _progressInfo.asStateFlow()

    private var compressedData = ArrayList<CompressedData>()
    private var decompressedData = ArrayList<DecompressedData>()

    private val warmUpRounds = 20
    private val measuredRounds = 2000

    init {
        simulateBackendCompression()
    }

    private fun simulateBackendCompression() {
        pageHelper.getPages().forEach { page ->
            compressedData.addAll(AlgorithmType.entries.map {
                val result = compressionHelper.compress(it, page.data.orEmpty())
                CompressedData(it.name, page.name, result)
            })
        }
    }

    fun decompress(type: AlgorithmType) {
        //decompressedData.clear()
        viewModelScope.launch(Dispatchers.Default) {
            pageHelper.getPages().forEach { page ->
                decompressedData.addAll(compressedData.filter {
                    it.algorithm == type.name && it.pageName == page.name
                }.map { compressedData ->
                    val timesTaken = ArrayList<Long>()
                    var result = ""

                    // Warm up
                    _progressInfo.value = "Warming up with $warmUpRounds rounds....."
                    repeat(warmUpRounds) {
                        compressionHelper.decompress(type, compressedData.data)
                    }
                    _progressInfo.value = "Warmup done!"

                    // Actual measurements
                    _progressInfo.value = "Decompressing with $measuredRounds rounds....."
                    for (i in 0..measuredRounds) {
                        Log.e("Measuring", "round: $i")
                        val startTime = System.currentTimeMillis()
                        result = compressionHelper.decompress(type, compressedData.data)
                        val endTime = System.currentTimeMillis()
                        val timeTaken = endTime - startTime
                        timesTaken.add(timeTaken)
                    }
                    val sizeInMBs = BigDecimal(result.toByteArray().size.toDouble() / (1024 * 1024))
                        .setScale(2, RoundingMode.HALF_EVEN)
                        .toDouble()
                    _progressInfo.value = "Decompression done."
                    DecompressedData(type.name, page.name, timesTaken, result, sizeInMBs)
                })
            }

            excelUtils.exportBenchMarkResultsToExcel(mutableListOf(BenchMarkData(type.name, decompressedData)), "benchmark_results.xlsx")

            _benchMarks.tryEmit(mutableListOf(BenchMarkData(type.name, decompressedData)))
        }
    }
}

enum class AlgorithmType {
    GZIP,
    SNAPPY,
    ZSTD
}