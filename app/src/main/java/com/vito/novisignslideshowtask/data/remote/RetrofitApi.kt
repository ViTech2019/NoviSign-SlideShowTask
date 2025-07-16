package com.vito.novisignslideshowtask.data.remote

import com.vito.novisignslideshowtask.helper.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitApi {

    val api: NoviSignApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NoviSignApi::class.java)
    }
}
