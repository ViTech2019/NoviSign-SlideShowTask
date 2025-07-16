package com.vito.novisignslideshowtask

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vito.novisignslideshowtask.data.remote.RetrofitApi
import com.vito.novisignslideshowtask.data.repo.PlaylistRepository
import com.vito.novisignslideshowtask.helper.Constants.PLAYLIST_REFRESH_INTERVAL_MS
import com.vito.novisignslideshowtask.helper.Constants.SCREEN_KEY
import com.vito.novisignslideshowtask.helper.Slide
import com.vito.novisignslideshowtask.helper.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SlideshowViewModel : ViewModel() {

    private val repository = PlaylistRepository(RetrofitApi.api)

    // current ui state
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    // current slide index
    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    // current slide start time
    private val _slideStartTime = MutableStateFlow(System.currentTimeMillis())
    val slideStartTime: StateFlow<Long> = _slideStartTime

    init {
        fetchPlaylistAndMediaPeriodically()
    }

    // fetches play list every minute and updates if MODIFIED has changed
    private fun fetchPlaylistAndMediaPeriodically() {
        viewModelScope.launch {
            var lastModified: Long? = null

            while (true) {
                try {
                    val playlistResponse = repository.fetchPlaylist(SCREEN_KEY) // fetch playlist
                    Log.d("ApiDebug", "Playlist response: $playlistResponse")

                    if (playlistResponse.modified != lastModified) { // check if playlist modified
                        lastModified = playlistResponse.modified

                        val items = playlistResponse.playlists
                            .flatMap { it.playlistItems }

                        if (items.isEmpty()) {
                            _uiState.value = UiState.Empty
                        } else {
                            val slides = items.map { item ->
                                val file = repository.downloadMediaFile(item.creativeKey)
                                Slide(
                                    file = file,
                                    durationSec = item.duration,
                                    label = item.creativeLabel
                                )
                            }
                            _uiState.value = UiState.Success(slides)

                            startSlideLoop(slides)// start loop slides when ready
                        }
                    }
                } catch (e: Exception) {
                    _uiState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
                }

                delay(PLAYLIST_REFRESH_INTERVAL_MS) // refreshes playlist every minute
            }
        }
    }

    private fun startSlideLoop(slides: List<Slide>) {
        viewModelScope.launch {
            while (true) {
                val slide = slides[_currentIndex.value]
                _slideStartTime.value = System.currentTimeMillis()

                delay(slide.durationSec * 1000L)

                // Move to next slide
                val nextIndex = (_currentIndex.value + 1) % slides.size
                _currentIndex.value = nextIndex
            }
        }
    }
}
