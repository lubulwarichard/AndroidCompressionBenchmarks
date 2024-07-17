package com.lubulwa.compressionbenchmarks.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lubulwa.compressionbenchmarks.ui.theme.CompressionBenchmarksTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompressionBenchmarksTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = innerPadding.calculateTopPadding()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BenchmarkOptions()
                        InfoView()
                        ShowBenchMarkResults()
                    }
                }
            }
        }
    }
}

@Composable
fun BenchmarkOptions(
    modifier: Modifier = Modifier,
    benchMarkViewModel: BenchMarkViewModel = viewModel()
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Button(
            onClick = { benchMarkViewModel.decompress(AlgorithmType.GZIP) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Gzip")
        }
        Button(onClick = { benchMarkViewModel.decompress(AlgorithmType.SNAPPY) },
            modifier = Modifier.padding(8.dp)) {
            Text(text = "Snappy")
        }
        Button(onClick = { benchMarkViewModel.decompress(AlgorithmType.ZSTD) },
            modifier = Modifier.padding(8.dp)) {
            Text(text = "ZStd")
        }
    }
}

@Composable
fun InfoView(
    modifier: Modifier = Modifier,
    benchMarkViewModel: BenchMarkViewModel = viewModel()
) {
    val currentText by benchMarkViewModel.progressInfo.collectAsState()

    Row(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
        ) {
            Text(text = currentText, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun ShowBenchMarkResults(
    modifier: Modifier = Modifier,
    benchMarkViewModel: BenchMarkViewModel = viewModel()
) {
    val items = benchMarkViewModel.benchMarks.collectAsStateWithLifecycle()
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(items.value) { item ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ) {
                Text(text = item.title, modifier = Modifier.fillMaxWidth())
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(color = Color.DarkGray)
            ) {
                Text(
                    text = "Page",
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    fontSize = 12.sp
                )
                Text(
                    text = "Duration(ms) - 30 rounds",
                    modifier = Modifier.weight(1.5f),
                    color = Color.White,
                    fontSize = 12.sp
                )
                Text(
                    text = "Size(Mbs)",
                    modifier = Modifier.weight(0.5f),
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
            item.data.forEach {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp)
                ) {
                    Text(
                        text = it.pageName,
                        modifier = Modifier.weight(1f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = it.timeTaken.joinToString(","),
                        modifier = Modifier.weight(1.5f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "${it.sizeInBytes}",
                        modifier = Modifier.weight(0.5f),
                        textAlign = TextAlign.End,
                        fontSize = 12.sp
                    )
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp)
                ) {
                    Text(
                        text = "Average",
                        modifier = Modifier.weight(1f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = it.timeTaken.average().toString(),
                        modifier = Modifier.weight(1.5f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "",
                        modifier = Modifier.weight(0.5f)
                    )
                }
                HorizontalDivider(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth(),
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CompressionBenchmarksTheme {
        BenchmarkOptions()
    }
}