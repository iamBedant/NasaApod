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
        is SaveButtonClickedEvent -> saveButtonClicked(model, event)
        PermissionGranted -> permissionGranted(model)
        PermissionDenied -> permissonDenied(model)
        PermissionDeniedForever -> permissionDeniedForever(model)
    }

fun permissonDenied(model: DownloadImageModel): Next<DownloadImageModel, DownloadImageEffect> {
    return Next.next(model.copy(permissionState = PermissionState.PERMISSION_DENIED))
}

fun permissionGranted(model: DownloadImageModel): Next<DownloadImageModel, DownloadImageEffect> {
    return Next.next(
        model.copy(permissionState = PermissionState.PERMISSION_GRANTED),
        Effects.effects(
            SaveImageEffect(model.imageUrl)
        )
    )
}

fun permissionDeniedForever(model: DownloadImageModel): Next<DownloadImageModel, DownloadImageEffect> {
    return Next.next(model.copy(permissionState = PermissionState.PERMISSION_DENIED_FOREVER))
}

fun saveButtonClicked(
    model: DownloadImageModel,
    event: SaveButtonClickedEvent
): Next<DownloadImageModel, DownloadImageEffect> {
    return Next.next(
        model.copy(
            imageUrl = event.url
        ), Effects.effects(CheckPermissionEffect)
    )
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
