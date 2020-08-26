package com.ps.photogallery.view

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ps.photogallery.R
import com.ps.photogallery.inflate
import com.ps.photogallery.loadImage
import com.ps.photogallery.service.Items
import kotlinx.android.synthetic.main.list_item.view.*

class PhotoListAdapter() : RecyclerView.Adapter<PhotoListAdapter.ViewHolder>() {

    var items: MutableList<Items> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflate(R.layout.list_item, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Items) {
            item.media?.image_url?.let { itemView.imageViewPhoto.loadImage(it) }
            // TODO Remove hardcoded string replace with String res
            itemView.photoDateTaken.text = "Date Captured:${item.date_taken}"
            itemView.photoDatePublished.text = "Date Published:${item.published}"
            itemView.photoAuthor.text = "Author: ${item.author}"

        }

    }
}
