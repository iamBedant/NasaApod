package com.iambedant.nasaapod.features.imageGallery.grid

import com.iambedant.nasaapod.data.model.ApodUI


/**
 * Created by @iamBedant on 06,August,2019
 */

interface IMobiusGalleryView {
    fun display(it: List<ApodUI>)
    fun scrollToPosition(currentItemPosition: Int)
    fun navigateToDetail()
    fun updateClickedItem(clickedItem: Int)
}