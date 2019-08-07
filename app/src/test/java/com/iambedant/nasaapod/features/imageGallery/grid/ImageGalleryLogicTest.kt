package com.iambedant.nasaapod.features.imageGallery.grid

import com.iambedant.nasaapod.data.apodUi
import com.iambedant.nasaapod.data.listOfApodUI
import com.iambedant.nasaapod.features.imageGallery.loadedGalleryModel
import com.spotify.mobius.test.FirstMatchers
import com.spotify.mobius.test.InitSpec
import com.spotify.mobius.test.InitSpec.assertThatFirst
import com.spotify.mobius.test.NextMatchers
import com.spotify.mobius.test.NextMatchers.*
import com.spotify.mobius.test.UpdateSpec
import com.spotify.mobius.test.UpdateSpec.assertThatNext
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
            .whenInit(loadedGalleryModel)
            .then(assertThatFirst(
                FirstMatchers.hasModel(GalleryModel(currentItemPosition = 5,loading = false, isError = false, listOfImages = listOfApodUI)),
                FirstMatchers.hasEffects(LoadGalleryEffect,ScrollToPositionEffect(5))
            ))
    }

    @Test
    fun `image Clicked Event should dispatch UpdateClicked and NavigateToPagerEffect`(){
        updateSpec
            .given(loadedGalleryModel)
            .whenEvent(ClickEvent(apodUi))
            .then(assertThatNext(
                hasNoModel(),
                NextMatchers.hasEffects(UpdateClickedItem(0),NavigateToPager)
            ))
    }

    @Test
    fun `imageLoaded event should dispetch no change if images is empty`(){
        updateSpec
            .given(loadedGalleryModel)
            .whenEvent(ImageLoaded(emptyList()))
            .then(assertThatNext(
                hasNothing()
            ))
    }

    @Test
    fun `imageLoaded event should dispetch no change if event images are same as current images`(){
        updateSpec
            .given(loadedGalleryModel)
            .whenEvent(ImageLoaded(loadedGalleryModel.listOfImages))
            .then(assertThatNext(
                hasNothing()
            ))
    }

    @Test
    fun `imageLoaded event should dispatch proper model in positive case`(){
        val newList = listOf(apodUi, apodUi)
        updateSpec
            .given(loadedGalleryModel)
            .whenEvent(ImageLoaded(newList))
            .then(assertThatNext(
                hasModel(loadedGalleryModel.copy(
                    listOfImages = newList,
                    loading = false,
                    isError = false
                ))
            ))
    }

    @Test
    fun `errorOccured Event should return the model with error set to true`(){
        updateSpec
            .given(loadedGalleryModel)
            .whenEvent(ErrorEvent)
            .then(assertThatNext(
                hasModel(loadedGalleryModel.copy(isError = true)),
                hasNoEffects()
            ))
    }


}