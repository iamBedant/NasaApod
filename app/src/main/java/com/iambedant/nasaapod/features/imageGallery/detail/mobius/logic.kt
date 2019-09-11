package com.iambedant.nasaapod.features.imageGallery.detail.mobius

import com.spotify.mobius.Effects
import com.spotify.mobius.Next

/**
 * Created by @iamBedant on 11,September,2019
 */


//fun init(model: GalleryImageModel): First<GalleryImageModel, DownloadImageEffect> =
//    First.first(GalleryImageModel())


fun update(
    model: DownloadImageModel,
    event: DownloadImageEvent
): Next<DownloadImageModel, DownloadImageEffect> =
    when (event) {
        is SaveImageEvent -> saveImage(model, event)
        ImageSavingSuccessEvent -> showSuccess()
        ImageSavingFailedEvent -> showFail()
    }

fun showFail(): Next<DownloadImageModel, DownloadImageEffect> {
    return Next.dispatch(Effects.effects(ShowResultMessage("Saving failed")))
}

fun showSuccess(): Next<DownloadImageModel, DownloadImageEffect> {
    return Next.dispatch(Effects.effects(ShowResultMessage("Saving Success")))
}

fun saveImage(
    model: DownloadImageModel,
    event: SaveImageEvent
): Next<DownloadImageModel, DownloadImageEffect> {
    return Next.next(model.copy(isDownloading = true), Effects.effects(SaveImageEffect(event.url)))
}
