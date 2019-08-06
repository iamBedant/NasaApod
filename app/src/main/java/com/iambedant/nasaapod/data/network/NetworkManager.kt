package com.iambedant.nasaapod.data.network

import com.iambedant.nasaapod.data.model.ApodNetworkResponse
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by @iamBedant on 06,August,2019
 */
class NetworkManager @Inject constructor(private val apiService: ApiService) : INetworkManager {
    override fun getApod(date: String): Single<ApodNetworkResponse> {
        return apiService.loadImage(date = date)
    }
}