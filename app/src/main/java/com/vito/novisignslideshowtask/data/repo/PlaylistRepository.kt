package com.vito.novisignslideshowtask.data.repo

import android.util.Log
import com.vito.novisignslideshowtask.data.model.PlaylistResponse
import com.vito.novisignslideshowtask.data.remote.NoviSignApi
import java.io.File

class PlaylistRepository(private val api: NoviSignApi) {

    // get the playlist to display slideshow
    suspend fun fetchPlaylist(screenKey: String): PlaylistResponse {
        return api.getPlaylist(screenKey)
    }

    // download media file by creativeKey
    suspend fun downloadMediaFile(creativeKey: String): File {
        val response = api.getMediaFile(creativeKey)
        Log.d(
            "ApiDebug",
            "Media file response for $creativeKey: ${response.code()} ${response.message()}"
        )
        if (response.isSuccessful) {
            val bytes = response.body()?.bytes()
            Log.d("ApiDebug", "Downloaded ${bytes?.size ?: 0} bytes for $creativeKey")
            val file = File.createTempFile(creativeKey, null)
            file.writeBytes(bytes ?: byteArrayOf())
            return file
        } else {
            throw Exception("Failed to download file: $creativeKey")
        }
    }
}
