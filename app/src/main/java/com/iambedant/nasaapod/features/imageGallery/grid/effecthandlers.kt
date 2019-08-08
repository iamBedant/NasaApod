package com.iambedant.nasaapod.features.imageGallery.grid

import com.iambedant.nasaapod.data.model.DbOperationFail
import com.iambedant.nasaapod.data.model.NetworkOperationFail
import com.iambedant.nasaapod.data.model.Success
import com.iambedant.nasaapod.data.repository.IGalleryRepository
import com.iambedant.nasaapod.utils.getListOfDates
import com.iambedant.nasaapod.utils.mobius.SubtypeEffectHandlerBuilder
import com.iambedant.nasaapod.utils.rx.ISchedulerProvider
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import java.util.concurrent.TimeUnit

/**
 * Created by @iamBedant on 06,August,2019
 */

fun createEffectHandler(
    view: IMobiusGalleryView,
    repository: IGalleryRepository,
    schedulerProvider: ISchedulerProvider
): ObservableTransformer<GalleryEffect, GalleryEvent> {

    return SubtypeEffectHandlerBuilder<GalleryEffect, GalleryEvent>()
        .addTransformer(loadGalleryEffectHandler(repository, schedulerProvider))
        .addTransformer(refreshImagesEffectHandler(repository, schedulerProvider))
        .addConsumer(navigateToPagerEffectHandler(view))
        .addConsumer(scrollToPositionEffectHandler(view))
        .addConsumer(updateClickedItemEffectHandler(view))
        .build()
}


fun updateClickedItemEffectHandler(view: IMobiusGalleryView): (UpdateClickedItem) -> Unit = {
    view.updateClickedItem(it.clickedItem)
}

fun navigateToPagerEffectHandler(view: IMobiusGalleryView): (NavigateToPager) -> Unit = {
    view.navigateToDetail()
}

fun scrollToPositionEffectHandler(view: IMobiusGalleryView): (ScrollToPositionEffect) -> Unit = {
    view.scrollToPosition(it.currentItemPosition)
}

fun loadGalleryEffectHandler(
    repository: IGalleryRepository,
    schedulerProvider: ISchedulerProvider
): ObservableTransformer<LoadGalleryEffect, GalleryEvent> =
    ObservableTransformer {
        it.flatMap {
            repository.getImages(getListOfDates())
                .toObservable()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .debounce(100, TimeUnit.MILLISECONDS)
                .map<GalleryEvent> { list ->
                    ImageLoaded(list)
                }
                .onErrorReturnItem(ErrorEvent)
        }
    }

fun refreshImagesEffectHandler(
    repository: IGalleryRepository,
    schedulerProvider: ISchedulerProvider
): ObservableTransformer<RefreshImagesEffect, GalleryEvent> =
    ObservableTransformer {
        it.flatMap {
            repository.refreshImagesV2(getListOfDates())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.computation())
                .flatMap { it ->
                    val listOfFailedNetworkRequests = mutableListOf<String>()
                    val listOfFailedDBRequests = mutableListOf<String>()
                    it.forEach {
                        when (it) {
                            is Success -> "do nothing"
                            is DbOperationFail -> listOfFailedDBRequests.add(it.date)
                            is NetworkOperationFail -> listOfFailedNetworkRequests.add(it.date)
                        }
                    }
                    val isSuccess = listOfFailedDBRequests.size + listOfFailedNetworkRequests.size == 0
                    return@flatMap Single.just(
                        ResultStatus(
                            status = if (isSuccess) RESULT_STATUS.SUCCESS else RESULT_STATUS.FAIL,
                            noOfFailedRequest = listOfFailedDBRequests.size + listOfFailedNetworkRequests.size
                        )
                    )
                }
                .toObservable()
                .observeOn(schedulerProvider.ui())
                .map<GalleryEvent> {
                    RefreshStatusEvent(it)
                }
                .onErrorReturnItem(ErrorEvent)
        }
    }




