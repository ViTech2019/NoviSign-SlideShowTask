package com.vito.novisignslideshowtask.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import com.vito.novisignslideshowtask.helper.Slide

@Composable
fun SlideshowPlayer(
    slides: List<Slide>,
    currentIndex: Int,
    slideStartTime: Long
) {
    val currentSlide = slides[currentIndex]

    Crossfade(
        targetState = currentSlide,
        animationSpec = tween(1000) // fade duration
    ) { slide ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = rememberAsyncImagePainter(slide.file),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
            SlideProgressIndicator(
                durationSec = slide.durationSec,
                slideStartTime = slideStartTime,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
    }
}

