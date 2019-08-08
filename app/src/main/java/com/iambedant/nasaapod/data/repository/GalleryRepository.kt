package com.iambedant.nasaapod.data.repository

import androidx.annotation.VisibleForTesting
import com.iambedant.nasaapod.data.model.*
import com.iambedant.nasaapod.data.network.INetworkManager
import com.iambedant.nasaapod.data.persistence.IPersistenceManager
import com.iambedant.nasaapod.utils.convertDbModelToUIModel
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by @iamBedant on 06,August,2019
 */
class GalleryRepository @Inject constructor(
    private val networkManager: INetworkManager,
    private val persistenceManager: IPersistenceManager
) : IGalleryRepository {

    override fun getImages(listOfDates: List<String>): Flowable<List<ApodUI>> {
        //TODO: Use the date list if we want to implement pagination
        return persistenceManager.loadImages()
            .flatMap {
                val listOfApodUi = mutableListOf<ApodUI>()
                it.forEach {
                    listOfApodUi.add(convertDbModelToUIModel(it))
                }
                return@flatMap Flowable.just(listOfApodUi)
            }
    }


    override fun refreshImagesV2(listOfDates: List<String>): Single<List<NetworkResult>> {
        val unavailableImageList = mutableListOf<Single<NetworkResult>>()
        listOfDates.forEach {
            if (!persistenceManager.isAvailable(it))
                unavailableImageList.add(callNetworkAndUpdateDb(it))
        }
        return Single.zip(unavailableImageList) { it: Array<Any> ->
            val list = mutableListOf<NetworkResult>()
            it.forEach {
                list.add(it as NetworkResult)
            }
            return@zip list
        }
    }


    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun callNetworkAndUpdateDb(date: String) =
        networkManager.getApod(date)
            .flatMap { response ->
                return@flatMap persistenceManager.saveImageToDb(response)
                    .toSingleDefault(Success as NetworkResult)
                    .onErrorReturn { DbOperationFail(response.date) }
            }.onErrorReturn { NetworkOperationFail(date) }

}





