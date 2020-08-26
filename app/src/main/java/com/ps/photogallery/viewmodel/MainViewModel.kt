package com.ps.photogallery.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ps.photogallery.dependency.ServiceLocator
import com.ps.photogallery.service.PhotoItem
import com.ps.photogallery.service.PhotoGalleryResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {

    var photoGalleryService = ServiceLocator.photoGalleryService

    private val compositeDisposable = CompositeDisposable()

    private val mutablePhotoList = MutableLiveData<List<PhotoItem>>()
    val photoListLiveData: LiveData<List<PhotoItem>>
        get() = mutablePhotoList

    private val mutableProgressLiveData = MutableLiveData<Boolean>()
    val progressLiveData: LiveData<Boolean>
        get() = mutableProgressLiveData

    private val mutableExceptionLiveData = MutableLiveData<ExceptionMessage>()
    val exceptionLiveData: LiveData<ExceptionMessage>
        get() = mutableExceptionLiveData

    fun fetchPublicPhotoList() {
        compositeDisposable.add(
            photoGalleryService.getPhotosPublic()
                .doOnSubscribe { mutableProgressLiveData.postValue(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map { response -> response.copy(items = response.items?.sortedBy { item -> item.dateCaptured }) }
                .doOnComplete { mutableProgressLiveData.postValue(false) }
                .subscribe(
                    this::handleSuccess,
                    this::handleException
                )
        )
    }

    private fun handleSuccess(photoGalleryResponse: PhotoGalleryResponse) {
        if (true == photoGalleryResponse.items?.isNotEmpty()) {
            mutablePhotoList.value = photoGalleryResponse.items
        } else {
            mutableExceptionLiveData.postValue(ExceptionMessage.EmptyList)
        }
    }

    private fun handleException(exception: Throwable) {
        mutableExceptionLiveData.postValue(
            ExceptionMessage.NetworkException(
                exception.message
                    ?: UNEXPECTED_ERROR
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun sortItemByDateCreate() {
        mutablePhotoList.value?.let {
            mutablePhotoList.postValue(it.sortedBy { item -> item.dateCaptured })
        }
    }

    fun sortItemByDatePublished() {
        mutablePhotoList.value?.let {
            mutablePhotoList.postValue(it.sortedBy { item -> item.datePublished })
        }
    }

    companion object {
        const val UNEXPECTED_ERROR = "Unexpected error"
    }

}

sealed class ExceptionMessage {
    object EmptyList : ExceptionMessage()
    class NetworkException(val message: String) : ExceptionMessage()
}