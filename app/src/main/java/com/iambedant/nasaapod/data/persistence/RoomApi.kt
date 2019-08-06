package com.iambedant.nasaapod.data.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iambedant.nasaapod.data.model.Apod
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by @iamBedant on 06,August,2019
 */

@Dao
interface RoomApi {

    @Query("SELECT * FROM apod")
    fun loadImages(): Flowable<List<Apod>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(apod: Apod) : Completable

    @Query("SELECT * FROM apod WHERE date = :date")
    fun loadImage(date:String): Flowable<Apod>

   @Query("SELECT COUNT(date) FROM apod WHERE date = :date")
    fun ifImageAvailable(date:String): Int
}