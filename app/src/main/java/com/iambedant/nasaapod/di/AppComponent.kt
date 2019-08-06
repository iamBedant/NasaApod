package com.iambedant.nasaapod.di

import com.iambedant.nasaapod.features.imageGallery.di.ImageGalleryComponent
import com.iambedant.nasaapod.features.imageGallery.di.ImageGalleryModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by @iamBedant on 06,August,2019
 */

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    operator fun plus(imageGalleryModule: ImageGalleryModule): ImageGalleryComponent
}