package com.iambedant.nasaapod.data.persistence


import com.iambedant.nasaapod.data.apod
import com.iambedant.nasaapod.data.apodNetworkResponse
import com.iambedant.nasaapod.data.listOfApod
import com.iambedant.nasaapod.data.model.Apod
import com.iambedant.nasaapod.utils.convertNetworkResponseToDbModel
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by @iamBedant on 06,August,2019
 */

class PersistenceManagerTest{
    @Mock
    private lateinit var roomApi: RoomApi
    private lateinit var persistencemanager : PersistenceManager

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        persistencemanager = PersistenceManager(roomApi)
    }

    @Test
    fun `isAvailable returns true if item is present in the db`(){
        whenever(roomApi.ifImageAvailable("Date")).thenReturn(1)
        assert(persistencemanager.isAvailable("Date"))
        verify(roomApi).ifImageAvailable("Date")
        verifyNoMoreInteractions(roomApi)
    }

    @Test
    fun `isAvailable returns false if item is not present in the db`(){
        whenever(roomApi.ifImageAvailable("Date")).thenReturn(0)
        assert(!persistencemanager.isAvailable("Date"))
        verify(roomApi).ifImageAvailable("Date")
        verifyNoMoreInteractions(roomApi)
    }

    @Test
    fun `saveImageToDb will call insertImage on roomApi with proper data`(){
        whenever(roomApi.insertImage(convertNetworkResponseToDbModel(apodNetworkResponse))).thenReturn(Completable.complete())
        val testObserver = TestObserver<Unit>()
        persistencemanager.saveImageToDb(apodNetworkResponse).subscribe(testObserver)
        verify(roomApi).insertImage(convertNetworkResponseToDbModel(apodNetworkResponse))
        testObserver.assertComplete()
        verifyNoMoreInteractions(roomApi)
    }

    @Test
    fun `loadImage should call loadImage on roomApi`(){
        whenever(roomApi.loadImage("Date")).thenReturn(Flowable.just(apod))
        val testSubscriber = TestSubscriber<Apod>()
        persistencemanager.loadImage("Date").subscribe(testSubscriber)
        verify(roomApi).loadImage("Date")
        testSubscriber.assertValue(apod)
        verifyNoMoreInteractions(roomApi)
    }

    @Test
    fun `loadImages should call loadImages on roomApi`(){
        whenever(roomApi.loadImages()).thenReturn(Flowable.just(listOfApod))
        val testSubscriber = TestSubscriber<List<Apod>>()
        persistencemanager.loadImages().subscribe(testSubscriber)
        verify(roomApi).loadImages()
        testSubscriber.assertValue(listOfApod)
        verifyNoMoreInteractions(roomApi)
    }

}