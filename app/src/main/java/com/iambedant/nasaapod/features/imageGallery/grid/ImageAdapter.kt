package com.iambedant.nasaapod.features.imageGallery.grid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.iambedant.nasaapod.R
import com.iambedant.nasaapod.data.model.ApodUI
import com.iambedant.nasaapod.utils.getShimmerDrawable


class ImageAdapter(
    private var images: List<ApodUI> = emptyList(), private val imageClicked: (id: ApodUI) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(
                R.layout.rv_item_grid,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val apod = images[position]
        if(apod.media_type=="image") {
            Glide.with(holder.image)
                .load(apod.url)
                .placeholder(getShimmerDrawable(holder.image.context))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.image)
        }
        holder.itemView.setOnClickListener { v ->
            imageClicked.invoke(apod)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun setMoments(spots: List<ApodUI>) {
        this.images = spots
    }

    fun getMoments(): List<ApodUI> {
        return images
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.imageView)
    }

}
