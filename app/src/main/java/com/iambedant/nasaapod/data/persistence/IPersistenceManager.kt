package com.iambedant.nasaapod.data.persistence

import com.iambedant.nasaapod.data.model.Apod
import com.iambedant.nasaapod.data.model.ApodNetworkResponse
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by @iamBedant on 06,August,2019
 */
interface IPersistenceManager {
    fun loadImages() : Flowable<List<Apod>>
    fun loadImage(date:String) : Flowable<Apod>
    fun saveImageToDb(it: ApodNetworkResponse) : Completable
    fun isAvailable(it: String) : Boolean
}