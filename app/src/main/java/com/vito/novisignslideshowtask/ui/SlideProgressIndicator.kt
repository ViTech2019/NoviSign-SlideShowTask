package com.vito.novisignslideshowtask.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vito.novisignslideshowtask.helper.Constants.SLIDE_PROGRESS_UPDATE_MS
import kotlinx.coroutines.delay

@Composable
fun SlideProgressIndicator(
    durationSec: Int,
    slideStartTime: Long,
    modifier: Modifier = Modifier
) {
    // keeps the current time in millis
    var currentTimeMillis by remember { mutableStateOf(System.currentTimeMillis()) }

    // launches coroutine to update the time every 32 millis
    LaunchedEffect(Unit) {
        while (true) {
            currentTimeMillis = System.currentTimeMillis()
            delay(SLIDE_PROGRESS_UPDATE_MS) // 32 millis
        }
    }

    val elapsedMillis = (currentTimeMillis - slideStartTime).coerceAtLeast(0) // time passed millis
    val progress = (1f - elapsedMillis / (durationSec * 1000f)).coerceIn(0f, 1f) // remaining progress
    val timeLeft = ((durationSec * 1000 - elapsedMillis) / 1000).toInt().coerceAtLeast(0) // time in sec left for countown

    Box(
        modifier = modifier
            .size(60.dp)
            .padding(8.dp)
    ) {
        CircularProgressIndicator(
            progress = { progress },
            strokeWidth = 4.dp,
            color = Color.Green,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = "${timeLeft}s",
            modifier = Modifier.align(Alignment.Center),
            color = Color.Green,
        )
    }
}

