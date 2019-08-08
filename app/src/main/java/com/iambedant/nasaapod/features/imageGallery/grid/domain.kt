package com.iambedant.nasaapod.features.imageGallery.grid

import com.iambedant.nasaapod.data.model.ApodUI

/**
 * Created by @iamBedant on 06,August,2019
 */

/**
 * Gallery Screen Model
 */

enum class RESULT_STATUS {
    SUCCESS,
    FAIL
}

data class ResultStatus(val status: RESULT_STATUS, val noOfFailedRequest: Int)


data class GalleryModel(
    val listOfImages: List<ApodUI> = emptyList(),
    val currentItemPosition: Int = 0,
    val loading: Boolean = true,
    val isError: Boolean = false,
    val isNetworkError: Boolean = false,
    val failedImage : Int = 0
)


/**
 * Gallery Screen Events
 */
sealed class GalleryEvent

data class ClickEvent(val clickedItem: ApodUI) : GalleryEvent()
data class ImageLoaded(val images: List<ApodUI>) : GalleryEvent()
object ErrorEvent : GalleryEvent()
object RetryEvent : GalleryEvent()
data class RefreshStatusEvent(val resultStatus: ResultStatus) : GalleryEvent()


/**
 * Gallery Screen Effects
 */
sealed class GalleryEffect

object NavigateToPager : GalleryEffect()
data class UpdateClickedItem(val clickedItem: Int) : GalleryEffect()
object LoadGalleryEffect : GalleryEffect()
object  RefreshImagesEffect : GalleryEffect()
data class ScrollToPositionEffect(val currentItemPosition: Int) : GalleryEffect()


