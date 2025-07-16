package com.vito.novisignslideshowtask.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vito.novisignslideshowtask.SlideshowViewModel
import com.vito.novisignslideshowtask.helper.UiState

@Composable
fun MainScreen(viewModel: SlideshowViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState() // ui state (loading, error, empty, success)
    val currentIndex by viewModel.currentIndex.collectAsState() // current slide index
    val slideStartTime by viewModel.slideStartTime.collectAsState() // start time of the current slide

    // display different content based on state
    when (state) {
        is UiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is UiState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${(state as UiState.Error).message}")
            }
        }
        is UiState.Empty -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No playlist items found.")
            }
        }
        is UiState.Success -> {
            val slides = (state as UiState.Success).slides
            SlideshowPlayer(
                slides = slides,
                currentIndex = currentIndex,
                slideStartTime = slideStartTime
            )
        }
    }
}
