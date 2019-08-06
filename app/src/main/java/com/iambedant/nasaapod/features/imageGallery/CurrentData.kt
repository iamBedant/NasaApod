package com.iambedant.nasaapod.features.imageGallery

import com.iambedant.nasaapod.data.model.ApodUI
import com.iambedant.nasaapod.features.imageGallery.grid.GalleryModel

/**
 * Created by @iamBedant on 06,August,2019
 */
object CurrentData {
    var list = listOf<ApodUI>()
    var currentPosition = 0
    var selectedPosition =0
    var model : GalleryModel? = null
}