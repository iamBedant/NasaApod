package com.iambedant.nasaapod.data.repository

import com.iambedant.nasaapod.data.apodNetworkResponse
import com.iambedant.nasaapod.data.listOfApod
import com.iambedant.nasaapod.data.model.*
import com.iambedant.nasaapod.data.network.INetworkManager
import com.iambedant.nasaapod.data.persistence.IPersistenceManager
import com.iambedant.nasaapod.data.someDate
import com.iambedant.nasaapod.data.someOtherDate
import com.iambedant.nasaapod.utils.convertDbModelToUIModel
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
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
    fun `refreshImagesV2`() {
        whenever(persistenceManager.isAvailable(someDate)).thenReturn(true)
        whenever(persistenceManager.isAvailable(someOtherDate)).thenReturn(false)
        whenever(netWorkManager.getApod(someOtherDate)).thenReturn(Single.just(apodNetworkResponse))
        whenever(persistenceManager.saveImageToDb(apodNetworkResponse)).thenReturn(Completable.complete())
        val testObserver = TestObserver<List<NetworkResult>>()
        galleryRepository.refreshImagesV2(listOf(someDate, someOtherDate)).subscribe(testObserver)
        testObserver.assertValue(listOf(Success))
        verify(persistenceManager).isAvailable(someOtherDate)
        verify(persistenceManager).isAvailable(someDate)
        verify(netWorkManager).getApod(someOtherDate)
        verify(persistenceManager).saveImageToDb(apodNetworkResponse)
        verifyNoMoreInteractions(persistenceManager,netWorkManager)
    }


    @Test
    fun `callNetworkAndUpdateDb should return Success in case of successful network and db operation`() {
        whenever(netWorkManager.getApod(someDate)).thenReturn(Single.just(apodNetworkResponse))
        whenever(persistenceManager.saveImageToDb(apodNetworkResponse)).thenReturn(Completable.complete())
        val testObserver = TestObserver<NetworkResult>()
        galleryRepository.callNetworkAndUpdateDb(someDate).subscribe(testObserver)
        testObserver.assertValue(Success)
        verify(netWorkManager).getApod(someDate)
        verify(persistenceManager).saveImageToDb(apodNetworkResponse)
        verifyNoMoreInteractions(persistenceManager, netWorkManager)
    }


    @Test
    fun `callNetworkAndUpdateDb should return DbOperationFail in case of db error`() {
        whenever(netWorkManager.getApod(someDate)).thenReturn(Single.just(apodNetworkResponse))
        whenever(persistenceManager.saveImageToDb(apodNetworkResponse)).thenReturn(Completable.error(Throwable("Some db error")))
        val testObserver = TestObserver<NetworkResult>()
        galleryRepository.callNetworkAndUpdateDb(someDate).subscribe(testObserver)
        testObserver.assertValue(DbOperationFail(apodNetworkResponse.date))
        verify(netWorkManager).getApod(someDate)
        verify(persistenceManager).saveImageToDb(apodNetworkResponse)
        verifyNoMoreInteractions(persistenceManager, netWorkManager)

    }


    @Test
    fun `callNetworkAndUpdateDb should return NetworkOperationFail in case of network error`() {
        whenever(netWorkManager.getApod(someDate)).thenReturn(Single.error(Throwable("SomeError")))
        val testObserver = TestObserver<NetworkResult>()
        galleryRepository.callNetworkAndUpdateDb(someDate).subscribe(testObserver)
        testObserver.assertValue(NetworkOperationFail(someDate))
        verify(netWorkManager).getApod(someDate)
        verifyNoMoreInteractions(persistenceManager, netWorkManager)
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
        verifyNoMoreInteractions(persistenceManager, netWorkManager)

    }

}