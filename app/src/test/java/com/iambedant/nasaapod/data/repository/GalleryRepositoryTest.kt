package com.iambedant.nasaapod.data.repository

import com.iambedant.nasaapod.data.apod
import com.iambedant.nasaapod.data.apodNetworkResponse
import com.iambedant.nasaapod.data.listOfApod
import com.iambedant.nasaapod.data.model.Apod
import com.iambedant.nasaapod.data.model.ApodUI
import com.iambedant.nasaapod.data.network.INetworkManager
import com.iambedant.nasaapod.data.persistence.IPersistenceManager
import com.iambedant.nasaapod.utils.convertDbModelToUIModel
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subscribers.TestSubscriber
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by @iamBedant on 06,August,2019
 */
class GalleryRepositoryTest {

    @Mock
    private lateinit var netWorkManager: INetworkManager
    @Mock
    private lateinit var persistenceManager: IPersistenceManager
    private lateinit var galleryRepository: GalleryRepository


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        galleryRepository = GalleryRepository(networkManager = netWorkManager, persistenceManager = persistenceManager)
    }


    @Test
    fun `loadImage should call loadImage on roomApi`() {
        whenever(persistenceManager.loadImage("Date")).thenReturn(Flowable.just(apod))
        val testSubscriber = TestSubscriber<Apod>()
        galleryRepository.loadImage("Date").subscribe(testSubscriber)
        verify(persistenceManager).loadImage("Date")
        testSubscriber.assertValue(apod)
        verifyNoMoreInteractions(persistenceManager, netWorkManager)
    }

    @Test
    fun `loadImages should call loadImages on roomApi`() {
        whenever(persistenceManager.loadImages()).thenReturn(Flowable.just(listOfApod))
        val testSubscriber = TestSubscriber<List<Apod>>()
        galleryRepository.loadImages().subscribe(testSubscriber)
        verify(persistenceManager).loadImages()
        testSubscriber.assertValue(listOfApod)
        verifyNoMoreInteractions(persistenceManager, netWorkManager)
    }

    @Test
    fun `refreshImage should call api and store response if data not available in cache`() {
        val dateList = listOf("Date1", "Date2")
        whenever(persistenceManager.isAvailable("Date1")).thenReturn(false)
        whenever(persistenceManager.isAvailable("Date2")).thenReturn(true)
        whenever(netWorkManager.getApod("Date1")).thenReturn(Single.just(apodNetworkResponse))
        val testSubscriber = TestSubscriber<Unit>()
        galleryRepository.refreshImages(dateList).subscribe(testSubscriber)
        verify(persistenceManager).isAvailable("Date1")
        verify(persistenceManager).isAvailable("Date2")
        verify(netWorkManager).getApod("Date1")
        verify(persistenceManager).saveImageToDb(apodNetworkResponse)
        verifyNoMoreInteractions(netWorkManager, persistenceManager)
    }


    @Test
    fun `fetchImageAndStore should call api and store the response to Db`() {
        whenever(netWorkManager.getApod("Date1")).thenReturn(Single.just(apodNetworkResponse))
        galleryRepository.fetchImageAndStore("Date1")
        verify(netWorkManager).getApod("Date1")
        verify(persistenceManager).saveImageToDb(apodNetworkResponse)
        verifyNoMoreInteractions(netWorkManager, persistenceManager)
    }


    @Test
    fun `getImages should call loadData on persistenceManager and convert it to proper Ui model`() {
        val uiModel = mutableListOf<ApodUI>()
        listOfApod.forEach {
            uiModel.add(convertDbModelToUIModel(it))
        }
        whenever(persistenceManager.loadImages()).thenReturn(Flowable.just(listOfApod))
        val testSubscriber = TestSubscriber<List<ApodUI>>()
        galleryRepository.getImages(listOf()).subscribe(testSubscriber)
        testSubscriber.assertValue(uiModel)
        verify(persistenceManager).loadImages()
        verifyNoMoreInteractions(persistenceManager,netWorkManager)

    }

}