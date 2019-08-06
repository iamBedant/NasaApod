package com.iambedant.nasaapod.features.imageGallery.di


import com.iambedant.nasaapod.GridScreen
import dagger.Subcomponent

/**
 * Created by @iamBedant on 06,August,2019
 */


@ImageGalleryScope
@Subcomponent(modules = [ImageGalleryModule::class])
interface ImageGalleryComponent {

    fun inject(gridScreen: GridScreen)

}