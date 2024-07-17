package com.lubulwa.compressionbenchmarks.utils

import android.content.Context
import com.lubulwa.compressionbenchmarks.model.PageData
import com.lubulwa.compressionbenchmarks.model.PageFile
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PageHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val pages: List<PageFile> = listOf(
        PageFile(name = "Home Root", fileName = "home_page_root"),
        PageFile(name = "Meals Root", fileName = "meals_page_root"),
        PageFile(name = "Promo Root", fileName = "promo_page_root"),
        PageFile(name = "Purchases Root", fileName = "purchases_page_root"),
        PageFile(name = "Search Results Kaas", fileName = "search_page_results_kaas"),
    )

    fun getPages(): List<PageData> {
        return pages.map {
            val fileData = loadFile("${it.fileName}.json")
            PageData(it.fileName, fileData)
        }
    }

    private fun loadFile(fileName: String): String? {
        try {
            val assetManager = context.assets
            val inputStream = assetManager.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            return String(buffer)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}