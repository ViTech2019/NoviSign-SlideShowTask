package com.vito.novisignslideshowtask.data.model

data class PlaylistResponse(
    val screenKey: String,
    val company: String?,
    val breakpointInterval: Int,
    val playlists: List<Playlist>,
    val modified: Long
)

data class Playlist(
    val channelTime: Int,
    val playlistKey: String,
    val playlistItems: List<PlaylistItem>
)

data class PlaylistItem(
    val creativeKey: String,
    val creativeLabel: String,
    val duration: Int
)
