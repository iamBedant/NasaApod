package com.iambedant.nasaapod.features.imageGallery.di

import com.iambedant.nasaapod.data.network.INetworkManager
import com.iambedant.nasaapod.data.persistence.IPersistenceManager
import com.iambedant.nasaapod.data.repository.GalleryRepository
import com.iambedant.nasaapod.data.repository.IGalleryRepository
import com.iambedant.nasaapod.utils.rx.ISchedulerProvider
import dagger.Module
import dagger.Provides

/**
 * Created by @iamBedant on 06,August,2019
 */


@Module
class ImageGalleryModule {
    @Provides
    fun provideGalleryRepository(
        networkManager: INetworkManager,
        persistenceManager: IPersistenceManager
    ): IGalleryRepository = GalleryRepository(networkManager, persistenceManager)


}