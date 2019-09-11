package com.iambedant.nasaapod.features.imageGallery.detail.mobius

import com.iambedant.nasaapod.features.imageGallery.detail.IGalleryImageDetailView
import com.iambedant.nasaapod.features.imageGallery.detail.repository.IGalleryImageDetailRepository
import com.iambedant.nasaapod.utils.mobius.SubtypeEffectHandlerBuilder
import com.iambedant.nasaapod.utils.rx.ISchedulerProvider
import io.reactivex.ObservableTransformer

/**
 * Created by @iamBedant on 11,September,2019
 */


fun createEffectHandler(
    view: IGalleryImageDetailView,
    repository: IGalleryImageDetailRepository,
    schedulerProvider: ISchedulerProvider
): ObservableTransformer<DownloadImageEffect, DownloadImageEvent> {

    return SubtypeEffectHandlerBuilder<DownloadImageEffect, DownloadImageEvent>()
        .addConsumer(showResultMessageEffectHandler(view), schedulerProvider.ui())
        .addAction<CheckPermissionEffect>(checkPermissionEffect(view), schedulerProvider.ui())
        .addTransformer(saveImageToGallery(repository, schedulerProvider))
        .build()
}

fun saveImageToGallery(
    repository: IGalleryImageDetailRepository,
    schedulerProvider: ISchedulerProvider
): ObservableTransformer<SaveImageEffect,DownloadImageEvent> {
    return ObservableTransformer {
        it.flatMap {
            repository.saveImageToGallery(it.url)
                .
        }
    }
}

fun checkPermissionEffect(view: IGalleryImageDetailView): () -> Unit {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}

fun showResultMessageEffectHandler(view: IGalleryImageDetailView): (ShowResultMessage) -> Unit {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}
