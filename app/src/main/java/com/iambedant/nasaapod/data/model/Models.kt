package com.iambedant.nasaapod.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iambedant.nasaapod.features.imageGallery.grid.RESULT_STATUS
import kotlinx.android.parcel.Parcelize

/**
 * Created by @iamBedant on 06,August,2019
 */

data class ApodNetworkResponse(
    val date: String,
    val explanation: String,
    val hdurl: String,
    val title: String,
    val media_type: String,
    val service_version: String,
    val url: String
)

@Entity(tableName = "apod")
data class Apod(
    @PrimaryKey
    val date: String,
    val explaination: String?,
    val hdurl: String?,
    val media_type: String,
    val title: String?,
    val url: String
)

@Parcelize
data class ApodUI (
    val date: String,
    val explaination: String,
    val media_type: String,
    val title: String,
    val url: String
) : Parcelable



sealed class NetworkResult
object Success : NetworkResult()
data class DbOperationFail(val date: String) : NetworkResult()
data class NetworkOperationFail(val date: String) : NetworkResult()

