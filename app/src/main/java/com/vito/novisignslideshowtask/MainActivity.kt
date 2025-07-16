package com.vito.novisignslideshowtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vito.novisignslideshowtask.ui.MainScreen
import com.vito.novisignslideshowtask.ui.theme.NoviSignSlideShowTaskTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoviSignSlideShowTaskTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding() // respect status and nav bars
                ) {
                    val viewModel: SlideshowViewModel = viewModel()
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}
