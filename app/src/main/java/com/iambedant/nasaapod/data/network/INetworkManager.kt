package com.iambedant.nasaapod.data.network

import com.iambedant.nasaapod.data.model.ApodNetworkResponse
import io.reactivex.Single

/**
 * Created by @iamBedant on 06,August,2019
 */
interface INetworkManager {
    fun getApod(date:String) : Single<ApodNetworkResponse>
}