package com.ps.photogallery.service

data class Media(val m: String)
data class Items(
    val title: String?,
    val media: Media?,
    val date_taken: String?,
    val description: String,
    val published: String?,
    val author: String?

)

data class PhotoGalleryResponse(
    val title: String?,
    val description: String,
    val modified: String?,
    val items: List<Items>?
)

