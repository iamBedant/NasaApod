package com.iambedant.nasaapod.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iambedant.nasaapod.data.model.Apod

/**
 * Created by @iamBedant on 06,August,2019
 */

@Database(entities = [(Apod::class)], version = 2,  exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): RoomApi
}