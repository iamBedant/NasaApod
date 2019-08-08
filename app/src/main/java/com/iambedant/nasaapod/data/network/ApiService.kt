package com.iambedant.nasaapod.data.network

import com.iambedant.nasaapod.BuildConfig
import com.iambedant.nasaapod.data.model.ApodNetworkResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by @iamBedant on 06,August,2019
 */
interface ApiService {
    @GET("/planetary/apod")
    fun loadImage(@Query("api_key") apiKey: String = BuildConfig.PASSWORD, @Query("date") date: String): Single<ApodNetworkResponse>
}