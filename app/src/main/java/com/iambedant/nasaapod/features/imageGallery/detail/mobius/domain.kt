package com.iambedant.nasaapod.features.imageGallery.detail.mobius

/**
 * Created by @iamBedant on 11,September,2019
 */


data class DownloadImageModel(
    val imageUrl: String = "",
    val isDownloading: Boolean = false
)




sealed class DownloadImageEvent
data class SaveImageEvent(val url: String): DownloadImageEvent()
object ImageSavingSuccessEvent : DownloadImageEvent()
object ImageSavingFailedEvent: DownloadImageEvent()



sealed class DownloadImageEffect
data class SaveImageEffect( val url: String) : DownloadImageEffect()
data class ShowResultMessage(val message: String) : DownloadImageEffect()