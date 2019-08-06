package com.iambedant.nasaapod.data

import com.iambedant.nasaapod.data.model.Apod
import com.iambedant.nasaapod.data.model.ApodNetworkResponse
import com.iambedant.nasaapod.data.model.ApodUI

/**
 * Created by @iamBedant on 06,August,2019
 */


val apodNetworkResponse = ApodNetworkResponse(
    date = "12-24-19",
    explanation = "This is some explaination",
    url = "http://url.com",
    title = "This is title",
    media_type = "image",
    hdurl = "some hd url",
    service_version = "1.4"
)

val apod = Apod(
    date = "12-24-19",
    hdurl = "http://hdurl.com",
    media_type = "image",
    title = "Title",
    url = "Url",
    explaination = "asd"
)

val listOfApod = listOf(apod)

val apodUi = ApodUI(
    date = "Date",
    url = "http://url",
    title = "This is Title,",
    media_type = "image",
    explaination = "this is explaination"
)

val listOfApodUI = listOf(apodUi)