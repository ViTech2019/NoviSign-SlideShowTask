package com.vito.novisignslideshowtask.helper

import java.io.File

sealed class UiState {
    object Loading : UiState()
    object Empty : UiState()
    data class Success(val slides: List<Slide>) : UiState()
    data class Error(val message: String) : UiState()
}

data class Slide(
    val file: File,
    val durationSec: Int,
    val label: String
)
