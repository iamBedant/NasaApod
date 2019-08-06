package com.iambedant.nasaapod.data.network

import com.iambedant.nasaapod.data.apodNetworkResponse
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by @iamBedant on 06,August,2019
 */
class NetworkManagerTest {

    @Mock
    private lateinit var apiService: ApiService
    private lateinit var netWorkManager : NetworkManager

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        netWorkManager = NetworkManager(apiService)
    }

    @Test
    fun `getApod should call loadImage of apiAervice`(){
        whenever(apiService.loadImage(date ="Date")).thenReturn(Single.just(apodNetworkResponse))
        netWorkManager.getApod("Date")
        verify(apiService).loadImage(date = "Date")
        verifyNoMoreInteractions(apiService)
    }
}