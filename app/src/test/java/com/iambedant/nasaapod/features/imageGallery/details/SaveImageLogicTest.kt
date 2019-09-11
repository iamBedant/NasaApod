package com.iambedant.nasaapod.features.imageGallery.details

import com.iambedant.nasaapod.features.imageGallery.detail.mobius.*
import com.iambedant.nasaapod.features.imageGallery.downloadImageModel
import com.iambedant.nasaapod.features.imageGallery.fakeUrl
import com.spotify.mobius.test.NextMatchers.*
import com.spotify.mobius.test.UpdateSpec
import com.spotify.mobius.test.UpdateSpec.assertThatNext
import org.junit.Test

/**
 * Created by @iamBedant on 11,September,2019
 */

class SaveImageLogicTest {

    private val updateSpec = UpdateSpec(::update)



    @Test
    fun `save image should update isLoading to true and dispatch SaveImageEffect with same url`(){
        updateSpec
            .given(downloadImageModel)
            .whenEvent(SaveImageEvent(fakeUrl))
            .then(assertThatNext(
                hasModel(downloadImageModel.copy(isDownloading = true)),
                hasEffects(SaveImageEffect(fakeUrl) as DownloadImageEffect)
            ))
    }

    @Test
    fun `ImageSavingSuccessEvent should dispatch ShowResultMessage with success message`(){
        updateSpec
            .given(downloadImageModel)
            .whenEvent(ImageSavingSuccessEvent)
            .then(assertThatNext(
                hasNoModel<DownloadImageModel,DownloadImageEffect>(),
                hasEffects(ShowResultMessage("Saving Success") as DownloadImageEffect)
            ))
    }

    @Test
    fun `ImageSavingFailedEvent should dispatch ShowResultMessage with an error message`(){
        updateSpec
            .given(downloadImageModel)
            .whenEvent(ImageSavingFailedEvent)
            .then(assertThatNext(
                hasNoModel<DownloadImageModel,DownloadImageEffect>(),
                hasEffects(ShowResultMessage("Saving failed") as DownloadImageEffect)
            ))
    }



}