package com.iambedant.nasaapod.features.imageGallery.grid

import com.spotify.mobius.Effects
import com.spotify.mobius.First
import com.spotify.mobius.Next

/**
 * Created by @iamBedant on 06,August,2019
 */

/**
 * Resumes execution from this point.
 */
fun init(model: GalleryModel): First<GalleryModel, GalleryEffect> =
    if (model.listOfImages.isNotEmpty()) {
        First.first(
            model, Effects.effects(
                LoadGalleryEffect,
                ScrollToPositionEffect(model.currentItemPosition)
            )
        )
    } else {
        First.first(
            model.copy(loading = true), Effects.effects(
                RefreshImagesEffect,
                LoadGalleryEffect
            )
        )
    }


fun update(model: GalleryModel, event: GalleryEvent): Next<GalleryModel, GalleryEffect> =
    when (event) {
        is ClickEvent -> imageClicked(model, event)
        is ImageLoaded -> onImageLoaded(model, event)
        is ErrorEvent -> errorOccurred(model)
        is RetryEvent -> retry(model)
        is RefreshStatusEvent -> refresh(model, event)
    }

fun refresh(model: GalleryModel, event: RefreshStatusEvent): Next<GalleryModel, GalleryEffect> {
    return if (event.resultStatus.status == RESULT_STATUS.FAIL) {
        Next.next(
            model.copy(
                isNetworkError = true,
                failedImage = event.resultStatus.noOfFailedRequest,
                loading = false
            )
        )
    } else {
        Next.next(model.copy(isNetworkError = false, failedImage = 0, loading = false))
    }
}

fun retry(model: GalleryModel): Next<GalleryModel, GalleryEffect> {
    return Next.next(
        model.copy(loading = true, isNetworkError = false, failedImage = 0),
        Effects.effects(RefreshImagesEffect)
    )
}

fun imageClicked(model: GalleryModel, event: ClickEvent): Next<GalleryModel, GalleryEffect> {
    return Next.dispatch(
        Effects.effects(
            UpdateClickedItem(
                model.listOfImages.indexOf(
                    event.clickedItem
                )
            ), NavigateToPager
        )
    )
}


fun errorOccurred(model: GalleryModel): Next<GalleryModel, GalleryEffect> {
    return Next.next(model.copy(isError = true))
}

fun onImageLoaded(model: GalleryModel, event: ImageLoaded): Next<GalleryModel, GalleryEffect> = when {
    event.images.isEmpty() -> Next.noChange()
    event.images == model.listOfImages -> Next.noChange()
    else -> Next.next(model.copy(listOfImages = event.images, loading = false, isError = false))
}

