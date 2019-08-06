package com.iambedant.nasaapod.utils

import com.iambedant.nasaapod.data.model.Apod
import com.iambedant.nasaapod.data.model.ApodNetworkResponse
import com.iambedant.nasaapod.data.model.ApodUI
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by @iamBedant on 06,August,2019
 */


fun getListOfDates(): List<String> {
    val listDate = mutableListOf<String>()
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    var fromDate = Date()
    val calendar = GregorianCalendar()
    calendar.time = fromDate
    for (i in 1..20) {
        listDate.add(sdf.format(calendar.time)).also { calendar.add(Calendar.DATE, -1) }
    }
    return listDate
}

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