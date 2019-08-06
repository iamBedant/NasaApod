package com.iambedant.nasaapod.data.repository

import androidx.annotation.VisibleForTesting
import com.iambedant.nasaapod.data.model.Apod
import com.iambedant.nasaapod.data.model.ApodUI
import com.iambedant.nasaapod.data.network.INetworkManager
import com.iambedant.nasaapod.data.persistence.IPersistenceManager
import com.iambedant.nasaapod.utils.convertDbModelToUIModel
import io.reactivex.Flowable
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by @iamBedant on 06,August,2019
 */
class GalleryRepository @Inject constructor(
    private val networkManager: INetworkManager,
    private val persistenceManager: IPersistenceManager
) : IGalleryRepository {
    override fun refreshImages(listOfDates: List<String>) = Flowable.fromIterable(listOfDates)
        .map {
            if (!persistenceManager.isAvailable(it)) {
                fetchImageAndStore(it)
            }
        }


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

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun fetchImageAndStore(date: String) {
        networkManager.getApod(date)
            .flatMapCompletable { persistenceManager.saveImageToDb(it) }
            .subscribe({ Timber.d("Saved Successfully") }, { Timber.e(it) })
    }


    override fun loadImage(date: String): Flowable<Apod> {
        return persistenceManager.loadImage(date)
    }

    override fun loadImages(): Flowable<List<Apod>> {
        return persistenceManager.loadImages()

    }

}