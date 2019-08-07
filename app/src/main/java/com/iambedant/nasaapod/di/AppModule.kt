package com.iambedant.nasaapod.di

import android.content.Context
import androidx.room.Room
import com.iambedant.nasaapod.BuildConfig
import com.iambedant.nasaapod.data.network.ApiService
import com.iambedant.nasaapod.data.network.INetworkManager
import com.iambedant.nasaapod.data.network.NetworkManager
import com.iambedant.nasaapod.data.persistence.AppDatabase
import com.iambedant.nasaapod.data.persistence.IPersistenceManager
import com.iambedant.nasaapod.data.persistence.PersistenceManager
import com.iambedant.nasaapod.data.persistence.RoomApi
import com.iambedant.nasaapod.utils.rx.ISchedulerProvider
import com.iambedant.nasaapod.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by @iamBedant on 06,August,2019
 */
@Module
class AppModule(private var context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context = context

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNetworkApi(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideNetworkManager(apiService: ApiService): INetworkManager = NetworkManager(apiService)

    @Provides
    fun provideSchedularProvider(): ISchedulerProvider = SchedulerProvider()

    @Singleton
    @Provides
    fun provideRoomDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "image-db").build()
    }

    @Singleton
    @Provides
    fun providesDao(database: AppDatabase): RoomApi = database.getDao()

    @Provides
    @Singleton
    fun providePersistenceManager(roomApi: RoomApi): IPersistenceManager = PersistenceManager(roomApi)
}