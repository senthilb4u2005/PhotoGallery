package com.ps.photogallery.dependency

import com.ps.photogallery.service.PhotoGalleryApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServiceLocator {
    private const val BASE_URL: String = "https://www.flickr.com/"

    val photoGalleryService: PhotoGalleryApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(PhotoGalleryApi::class.java)
    }
}