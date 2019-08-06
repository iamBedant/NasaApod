package com.iambedant.nasaapod.data.utils

import com.iambedant.nasaapod.data.model.Apod
import com.iambedant.nasaapod.data.model.ApodNetworkResponse
import com.iambedant.nasaapod.data.model.ApodUI
import com.iambedant.nasaapod.utils.convertDbModelToUIModel
import com.iambedant.nasaapod.utils.convertNetworkResponseToDbModel
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by @iamBedant on 06,August,2019
 */


class CommonUtilsTest {

    @Test
    fun `convertNetworkResponseToDbModel test`() {
        val date = "Date1"
        val explaination = "This is an explaination"
        val mediaType = "audio"
        val hdUrl = "http://url"
        val title = "Title"
        val url = "http://url"
        val apod = Apod(
            date = date,
            explaination = explaination,
            media_type = mediaType,
            hdurl = hdUrl,
            title = title,
            url = url
        )

        val networkResponse = ApodNetworkResponse(
            date = date,
            url = url,
            title = title,
            hdurl = hdUrl,
            media_type = mediaType,
            service_version = "4",
            explanation = explaination
        )
        assertEquals(convertNetworkResponseToDbModel(networkResponse), apod)
    }

    @Test
    fun `convertDbModelToUIModel test`() {
        val date = "Date1"
        val explaination = null
        val mediaType = "audio"
        val hdUrl = null
        val title = "Title"
        val url = "http://url"
        val apod = Apod(
            date = date,
            explaination = explaination,
            media_type = mediaType,
            hdurl = hdUrl,
            title = title,
            url = url
        )

        val apodUi = ApodUI(
            date = date,
            media_type = mediaType,
            title = title,
            url = url,
            explaination = ""
        )

        assertEquals(apodUi,convertDbModelToUIModel(apod))
    }

}