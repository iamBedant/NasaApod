package com.iambedant.nasaapod.features.imageGallery.grid

import com.iambedant.nasaapod.data.model.ApodUI

/**
 * Created by @iamBedant on 06,August,2019
 */

/**
 * Gallery Screen Model
 */

data class GalleryModel(
    val listOfImages: List<ApodUI> = emptyList(),
    val currentItemPosition: Int = 0,
    val loading: Boolean = true,
    val isError: Boolean = false
)


/**
 * Gallery Screen Events
 */
sealed class GalleryEvent

data class ClickEvent(val clickedItem: ApodUI) : GalleryEvent()
data class ImageLoaded(val images: List<ApodUI>) : GalleryEvent()
object ErrorEvent : GalleryEvent()


/**
 * Gallery Screen Effects
 */
sealed class GalleryEffect

object NavigateToPager : GalleryEffect()
data class UpdateClickedItem(val clickedItem: Int) : GalleryEffect()
object LoadGalleryEffect : GalleryEffect()
object RefreshImagesEffect : GalleryEffect()
data class ScrollToPositionEffect(val currentItemPosition: Int) : GalleryEffect()


