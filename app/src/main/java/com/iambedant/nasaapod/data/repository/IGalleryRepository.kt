package com.iambedant.nasaapod.data.repository

import com.iambedant.nasaapod.data.model.ApodUI
import com.iambedant.nasaapod.data.model.NetworkResult
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by @iamBedant on 06,August,2019
 */
interface IGalleryRepository {
    fun getImages(listOfDates: List<String>) : Flowable<List<ApodUI>>
    fun refreshImagesV2(listOfDates: List<String>): Single<List<NetworkResult>>
}