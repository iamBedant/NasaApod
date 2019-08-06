package com.iambedant.nasaapod.utils

import com.iambedant.nasaapod.data.model.Apod
import com.iambedant.nasaapod.data.model.ApodNetworkResponse
import com.iambedant.nasaapod.data.model.ApodUI

/**
 * Created by @iamBedant on 06,August,2019
 */


fun convertNetworkResponseToDbModel(it: ApodNetworkResponse) = Apod(
    date = it.date,
    explaination = it.explanation,
    media_type = it.media_type,
    hdurl = it.hdurl,
    title = it.title,
    url = it.url
)

fun convertDbModelToUIModel(it: Apod): ApodUI = ApodUI(
    date =  it.date,
    explaination =  it.explaination ?: "",
    media_type = it.media_type,
    title =  it.title ?: "",
    url =  it.url
)