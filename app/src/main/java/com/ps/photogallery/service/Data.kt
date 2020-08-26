package com.ps.photogallery.service

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Media(@SerializedName("m") val image_url: String)
data class Items(
    val title: String?,
    val media: Media?,
    @SerializedName("date_taken")
    val dateCaptured: Date?,
    val description: String,
    @SerializedName("published")
    val datePublished: Date?,
    val author: String?

)

data class PhotoGalleryResponse(
    val title: String?,
    val description: String,
    val modified: String?,
    val items: List<Items>?
)

