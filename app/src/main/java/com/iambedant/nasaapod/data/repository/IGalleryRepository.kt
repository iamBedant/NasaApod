package com.iambedant.nasaapod.data.repository

import com.iambedant.nasaapod.data.model.Apod
import com.iambedant.nasaapod.data.model.ApodUI
import io.reactivex.Flowable

/**
 * Created by @iamBedant on 06,August,2019
 */
interface IGalleryRepository {
    fun loadImages(): Flowable<List<Apod>>
    fun loadImage(date: String): Flowable<Apod>
    fun getImages(listOfDates: List<String>) : Flowable<List<ApodUI>>
    fun refreshImages(listOfDates: List<String>): Flowable<Unit>
}