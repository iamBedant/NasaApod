package com.iambedant.nasaapod.data.persistence

import com.iambedant.nasaapod.data.model.Apod
import com.iambedant.nasaapod.data.model.ApodNetworkResponse
import com.iambedant.nasaapod.utils.convertNetworkResponseToDbModel
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Created by @iamBedant on 06,August,2019
 */
class PersistenceManager @Inject constructor(private val roomApi: RoomApi) : IPersistenceManager {
    override fun isAvailable(it: String): Boolean {
        return roomApi.ifImageAvailable(it) > 0
    }

    override fun saveImageToDb(it: ApodNetworkResponse) : Completable{
        return roomApi.insertImage(
            convertNetworkResponseToDbModel(it)
        )
    }

    override fun loadImage(date: String): Flowable<Apod> {
        return roomApi.loadImage(date)
    }

    override fun loadImages(): Flowable<List<Apod>> {
        return roomApi.loadImages()
    }
}