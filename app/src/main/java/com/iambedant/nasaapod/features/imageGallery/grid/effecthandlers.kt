package com.iambedant.nasaapod.features.imageGallery.grid

import com.iambedant.nasaapod.data.repository.IGalleryRepository
import com.iambedant.nasaapod.utils.getListOfDates
import com.iambedant.nasaapod.utils.mobius.SubtypeEffectHandlerBuilder
import com.iambedant.nasaapod.utils.rx.ISchedulerProvider
import io.reactivex.ObservableTransformer
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
        .addConsumer(refreshImagesEffectHandler(repository), schedulerProvider.io())
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

fun refreshImagesEffectHandler(repository: IGalleryRepository): (RefreshImagesEffect) -> Unit = {
    repository.refreshImages(getListOfDates())
        .subscribe()
}



