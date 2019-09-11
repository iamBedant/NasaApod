package com.iambedant.nasaapod.features.imageGallery.detail.mobius

/**
 * Created by @iamBedant on 11,September,2019
 */


enum class PermissionState{
    PERMISSION_GRANTED,
    PERMISSION_DENIED,
    PERMISSION_DENIED_FOREVER
}


data class DownloadImageModel(
    val imageUrl: String = "",
    val isDownloading: Boolean = false,
    val permissionState: PermissionState
)




sealed class DownloadImageEvent
data class SaveImageEvent(val url: String): DownloadImageEvent()
object ImageSavingSuccessEvent : DownloadImageEvent()
object ImageSavingFailedEvent: DownloadImageEvent()
data class SaveButtonClickedEvent(val url: String) : DownloadImageEvent()
object PermissionGranted : DownloadImageEvent()
object PermissionDenied : DownloadImageEvent()
object PermissionDeniedForever : DownloadImageEvent()


sealed class DownloadImageEffect
data class SaveImageEffect( val url: String) : DownloadImageEffect()
data class ShowResultMessage(val message: String) : DownloadImageEffect()
object CheckPermissionEffect : DownloadImageEffect()



