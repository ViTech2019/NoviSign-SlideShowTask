package com.vito.novisignslideshowtask.data.remote

import com.vito.novisignslideshowtask.data.model.PlaylistResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface NoviSignApi {

    @GET("PlayerBackend/screen/playlistItems/{screenKey}")
    suspend fun getPlaylist(@Path("screenKey") screenKey: String): PlaylistResponse

    @GET("PlayerBackend/creative/get/{creativeKey}")
    suspend fun getMediaFile(@Path("creativeKey") creativeKey: String): Response<ResponseBody>
}
