package com.openclassrooms.realestatemanager.presentation.realestateupdate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.openclassrooms.realestatemanager.databinding.PhotoItemBinding
import com.openclassrooms.realestatemanager.domain.models.Photo

class PhotoAdapter(
    private val photos: MutableList<Photo>,
    private val listener: ListPhotoListener
) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            PhotoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    override fun getItemCount(): Int = photos.size

    inner class ViewHolder(private val binding: PhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo) {
            binding.photoItemTvDescription.text = photo.description
            binding.photoItemIvImage.load(photo.uri)
            binding.photoDescriptionItem.setOnClickListener {
                listener.onPhotoItemSelected(photo,absoluteAdapterPosition)
            }
        }
    }
}

interface ListPhotoListener {
    fun onPhotoItemSelected(photo: Photo, position: Int)
}