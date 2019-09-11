package com.iambedant.nasaapod.features.imageGallery

import com.iambedant.nasaapod.data.listOfApodUI
import com.iambedant.nasaapod.features.imageGallery.detail.mobius.DownloadImageModel
import com.iambedant.nasaapod.features.imageGallery.detail.mobius.PermissionState
import com.iambedant.nasaapod.features.imageGallery.grid.GalleryModel

/**
 * Created by @iamBedant on 07,August,2019
 */

val loadedGalleryModel = GalleryModel(currentItemPosition = 5, loading = false, isError = false, listOfImages = listOfApodUI)
val downloadImageModel = DownloadImageModel(isDownloading = false, imageUrl = "", permissionState = PermissionState.PERMISSION_DENIED)
val fakeUrl ="image url"