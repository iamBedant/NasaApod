package com.iambedant.nasaapod

import android.app.Application
import com.iambedant.nasaapod.di.AppComponent
import com.iambedant.nasaapod.di.AppModule
import com.iambedant.nasaapod.di.DaggerAppComponent
import com.iambedant.nasaapod.features.imageGallery.di.ImageGalleryComponent
import com.iambedant.nasaapod.features.imageGallery.di.ImageGalleryModule
import timber.log.Timber

/**
 * Created by @iamBedant on 06,August,2019
 */


class NasaApodApp : Application() {

    var appComponent: AppComponent? = null
        private set
    private var imageGalleryComponent: ImageGalleryComponent? = null

    override fun onCreate() {
        super.onCreate()
        appComponent = createAppComponent()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            //TODO: May be plant a custom release tree
        }
    }

    private fun createAppComponent(): AppComponent {
        return DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    fun createonImageGalleryComponent(): ImageGalleryComponent? {
        imageGalleryComponent = appComponent?.plus(ImageGalleryModule())
        return imageGalleryComponent
    }

    fun releaseImageGalleryComponent() {
        imageGalleryComponent = null
    }
}