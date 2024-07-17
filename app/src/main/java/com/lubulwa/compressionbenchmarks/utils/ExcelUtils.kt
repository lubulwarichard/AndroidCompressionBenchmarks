package com.lubulwa.compressionbenchmarks.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.lubulwa.compressionbenchmarks.model.BenchMarkData
import dagger.hilt.android.qualifiers.ApplicationContext
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ExcelUtils @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun exportBenchMarkResultsToExcel(
        data: List<BenchMarkData>,
        fileName: String
    ) {
        val wb = XSSFWorkbook()
        val sheet = wb.createSheet(fileName)

        // Set up headers
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Page")
        headerRow.createCell(1).setCellValue("Duration(ms) - 30 rounds")
        headerRow.createCell(2).setCellValue("Average Time")
        headerRow.createCell(3).setCellValue("Size(Mbs)")

        // Add data rows
        var rowIndex = 1
        data.forEach { benchmark ->
            val dataRow = sheet.createRow(rowIndex)
            dataRow.createCell(0).setCellValue(benchmark.title)
            rowIndex++

            benchmark.data.forEach { result ->
                val resultRow = sheet.createRow(rowIndex)
                resultRow.createCell(0).setCellValue(result.pageName)
                resultRow.createCell(1).setCellValue(result.timeTaken.joinToString(","))
                resultRow.createCell(2).setCellValue(result.timeTaken.average().round(3).toString())
                resultRow.createCell(3).setCellValue(result.sizeInBytes.toString())
                rowIndex++
            }

            rowIndex++

            // Add a divider row
            val dividerRow = sheet.createRow(rowIndex++)
            val dividerCell = dividerRow.createCell(0)
        }

        // Write the data to a file with permission checks
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val file = File(context.filesDir, fileName)
            val fileOutputStream = FileOutputStream(file)
            wb.write(fileOutputStream)
            fileOutputStream.close()
            Handler(Looper.getMainLooper()).post {
                // Handle permission denied case (e.g., show a toast)
                Toast.makeText(
                    context,
                    "File created",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Handler(Looper.getMainLooper()).post {
                // Handle permission denied case (e.g., show a toast)
                Toast.makeText(
                    context,
                    "Storage permission required for export!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()