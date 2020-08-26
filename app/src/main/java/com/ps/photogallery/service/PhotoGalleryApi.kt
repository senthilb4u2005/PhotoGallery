package com.ps.photogallery.service

import io.reactivex.Observable
import retrofit2.http.GET

interface PhotoGalleryApi {

    @GET("services/feeds/photos_public.gne?format=json&nojsoncallback=1")
    fun getPhotosPublic(): Observable<PhotoGalleryResponse>
}