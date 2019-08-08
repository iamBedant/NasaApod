package com.iambedant.nasaapod.features.imageGallery.grid

import com.iambedant.nasaapod.data.model.ApodUI
import com.iambedant.nasaapod.data.model.DbOperationFail
import com.iambedant.nasaapod.data.model.NetworkOperationFail
import com.iambedant.nasaapod.data.model.Success
import com.iambedant.nasaapod.data.repository.IGalleryRepository
import com.iambedant.nasaapod.data.someDate
import com.iambedant.nasaapod.data.someOtherDate
import com.iambedant.nasaapod.utils.getListOfDates
import com.iambedant.nasaapod.utils.rx.ISchedulerProvider
import com.iambedant.nasaapod.utils.rx.ImmediateSchedulerProvider
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by @iamBedant on 06,August,2019
 */
object ImageGalleryEffectsTest {

    class LoadGalleryEffectTest {

        @Mock
        lateinit var view: IMobiusGalleryView

        @Mock
        lateinit var repository: IGalleryRepository

        lateinit var schedulerProvider : ISchedulerProvider

        @Before
        fun setUp() {
            MockitoAnnotations.initMocks(this)
            schedulerProvider = ImmediateSchedulerProvider()
        }

        @Test
        fun `LoadGalleryEffect emmits ImageLoadedEvent in success case`() {
            val listOfImages = listOf<ApodUI>()
            whenever(repository.getImages(getListOfDates())).thenReturn(Flowable.just(listOfImages))
            val testCase = TestCase(loadGalleryEffectHandler(repository, schedulerProvider))
            testCase.dispatchEffect(LoadGalleryEffect)
            testCase.assertEvents(ImageLoaded(listOfImages))
            verify(repository).getImages(getListOfDates())
            verifyNoMoreInteractions(repository,view)
        }

        @Test
        fun `LoadGalleryEffect emmits ErrorEvent in case of error`() {
            whenever(repository.getImages(getListOfDates())).thenReturn(Flowable.error(Throwable("Something wrong")))
            val testCase = TestCase(loadGalleryEffectHandler(repository, schedulerProvider))
            testCase.dispatchEffect(LoadGalleryEffect)
            testCase.assertEvents(ErrorEvent)
            verify(repository).getImages(getListOfDates())
            verifyNoMoreInteractions(repository,view)
        }

        @Test
        fun `updateClickedItem should call updateClicked on view`() {
            val updateClickConsumer = updateClickedItemEffectHandler(view)
            updateClickConsumer(UpdateClickedItem(3))
            verify(view).updateClickedItem(3)
            verifyNoMoreInteractions(repository,view)
        }

        @Test
        fun `navigateToPager should call navigateToDetail on view`(){
            val navigateToPagerConsumer = navigateToPagerEffectHandler(view)
            navigateToPagerConsumer(NavigateToPager)
            verify(view).navigateToDetail()
            verifyNoMoreInteractions(repository,view)
        }

        @Test
        fun `scrollToPositionEffectHandler should call scrollToPosition on view`(){
            val scrollToPositionConsumer = scrollToPositionEffectHandler(view)
            scrollToPositionConsumer(ScrollToPositionEffect(5))
            verify(view).scrollToPosition(5)
            verifyNoMoreInteractions(repository,view)
        }


        @Test
        fun `refreshImagesEffectHandler emmits RefreshStatusEven with RESULT_STATUS_FAIL in some error case`() {
            whenever(repository.refreshImagesV2(getListOfDates())).thenReturn(Single.just(listOf(Success, DbOperationFail(date = someDate), NetworkOperationFail(date = someOtherDate))))
            val testCase = TestCase(refreshImagesEffectHandler(repository, schedulerProvider))
            testCase.dispatchEffect(RefreshImagesEffect)
            testCase.assertEvents(RefreshStatusEvent(ResultStatus(status = RESULT_STATUS.FAIL, noOfFailedRequest = 2)))
            verify(repository).refreshImagesV2(getListOfDates())
            verifyNoMoreInteractions(repository,view)
        }

        @Test
        fun `refreshImagesEffectHandler emmits RefreshStatusEven with RESULT_STATUS_SUCCESS in positive case`() {
            whenever(repository.refreshImagesV2(getListOfDates())).thenReturn(Single.just(listOf(Success, Success, Success)))
            val testCase = TestCase(refreshImagesEffectHandler(repository, schedulerProvider))
            testCase.dispatchEffect(RefreshImagesEffect)
            testCase.assertEvents(RefreshStatusEvent(ResultStatus(status = RESULT_STATUS.SUCCESS, noOfFailedRequest = 0)))
            verify(repository).refreshImagesV2(getListOfDates())
            verifyNoMoreInteractions(repository,view)
        }



        @Test
        fun `refreshImagesEffectHandler emmits ErrorEvent in case of error`() {
            whenever(repository.refreshImagesV2(getListOfDates())).thenReturn(Single.error(Throwable("Something wrong")))
            val testCase = TestCase(refreshImagesEffectHandler(repository, schedulerProvider))
            testCase.dispatchEffect(RefreshImagesEffect)
            testCase.assertEvents(ErrorEvent)
            verify(repository).refreshImagesV2(getListOfDates())
            verifyNoMoreInteractions(repository,view)
        }



    }


    internal class TestCase<F, E>(underTest: ObservableTransformer<F, E>) {
        val upstream = PublishSubject.create<F>()
        val observer = TestObserver<E>()

        init {
            upstream.compose(underTest).subscribe(observer)
        }

        fun dispatchEffect(effect: F) {
            upstream.onNext(effect)
        }

        @SafeVarargs
        fun assertEvents(vararg events: E) {
            observer.assertValues(*events)
        }
    }
}