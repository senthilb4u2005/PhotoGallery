package com.ps.photogallery.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ps.photogallery.getOrAwaitValue
import com.ps.photogallery.service.Media
import com.ps.photogallery.service.PhotoGalleryApi
import com.ps.photogallery.service.PhotoGalleryResponse
import com.ps.photogallery.service.PhotoItem
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

class MainViewModelTest {


    @Mock
    lateinit var service: PhotoGalleryApi

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var subject: MainViewModel


    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        MockitoAnnotations.initMocks(this)

        subject = MainViewModel()
        subject.photoGalleryService = service
    }


    @Test
    fun verify_network_exception_is_passed_through_livedata() {

        val error = "Error"
        Mockito.`when`(service.getPhotosPublic())
            .thenReturn(Observable.error(Throwable(error)))
        subject.fetchPublicPhotoList()
        val throwable = subject.exceptionLiveData.getOrAwaitValue()
        val message = if (throwable is ExceptionMessage.NetworkException) throwable.message else ""
        assertEquals(error, message)


    }

    @Test
    fun verify_network_exception_when_message_is_null() {
        val error = "Unexpected error"
        Mockito.`when`(service.getPhotosPublic())
            .thenReturn(Observable.error(Throwable(message = null)))
        subject.fetchPublicPhotoList()
        val throwable = subject.exceptionLiveData.getOrAwaitValue()
        val message = if (throwable is ExceptionMessage.NetworkException) throwable.message else ""
        assertEquals(error, message)

    }

    @Test
    fun verify_service_return_empty_list_and_error_shown() {

        val response = PhotoGalleryResponse("", "", "", arrayListOf())
        Mockito.`when`(service.getPhotosPublic())
            .thenReturn(Observable.just(response))
        subject.fetchPublicPhotoList()
        val throwable = subject.exceptionLiveData.getOrAwaitValue()
        assert(throwable is ExceptionMessage.EmptyList)

    }

    @Test
    fun verify_api_service_success_response() {
        val response = getMockPhotoGalleryResponse()
        Mockito.`when`(service.getPhotosPublic())
            .thenReturn(Observable.just(response))
        subject.fetchPublicPhotoList()
        val items = subject.photoListLiveData.getOrAwaitValue()
        assertEquals(response.items, items)
    }

    @Test
    fun when_calling_api_service_verify_progress_is_shown() {
        val response = getMockPhotoGalleryResponse()
        Mockito.`when`(service.getPhotosPublic())
            .thenReturn(Observable.just(response))
        subject.fetchPublicPhotoList()
        val isProgressing = subject.progressLiveData.getOrAwaitValue()
        assertEquals(true, isProgressing)

    }


    @After
    fun finalize() {
        RxAndroidPlugins.reset()
    }

    fun getMockPhotoGalleryResponse(): PhotoGalleryResponse {

         val date = Date(System.currentTimeMillis())


        val items = arrayListOf(
            PhotoItem(
                "Title1",
                Media("url"),
                date,
                "Description",
                date,
                "Author"
            ),
            PhotoItem(
                "Title1",
                Media("url"),
                date,
                "Description",
                date,
                "Author"
            ),
            PhotoItem(
                "Title1",
                Media("url"),
                date,
                "Description",
                date,
                "Author"
            )
        )

        return PhotoGalleryResponse("title", "Description", "", items)
    }

}