package com.iambedant.nasaapod.features.imageGallery.grid

import com.iambedant.nasaapod.data.listOfApodUI
import com.spotify.mobius.test.FirstMatchers
import com.spotify.mobius.test.InitSpec
import com.spotify.mobius.test.InitSpec.assertThatFirst
import com.spotify.mobius.test.UpdateSpec
import org.junit.Test

/**
 * Created by @iamBedant on 06,August,2019
 */


class ImageGalleryLogicTest{
    private val initSpec = InitSpec(::init)
    private val updateSpec = UpdateSpec(::update)

    @Test
    fun `init without dataLoad remote and local data`(){
        initSpec
            .whenInit(GalleryModel())
            .then(assertThatFirst(
                FirstMatchers.hasModel(GalleryModel(loading = true)),
                FirstMatchers.hasEffects(RefreshImagesEffect,LoadGalleryEffect)
            ))
    }

    @Test
    fun `init with dataLoad remote and local data`(){
        initSpec
            .whenInit(GalleryModel(currentItemPosition = 5, loading = false, isError = false, listOfImages = listOfApodUI))
            .then(assertThatFirst(
                FirstMatchers.hasModel(GalleryModel(currentItemPosition = 5,loading = false, isError = false, listOfImages = listOfApodUI)),
                FirstMatchers.hasEffects(LoadGalleryEffect,ScrollToPositionEffect(5))
            ))
    }



}