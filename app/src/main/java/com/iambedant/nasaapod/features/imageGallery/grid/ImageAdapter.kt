package com.iambedant.nasaapod.features.imageGallery.grid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iambedant.nasaapod.R
import com.iambedant.nasaapod.data.model.ApodUI


class ImageAdapter(
    private var moments: List<ApodUI> = emptyList(), private val momentInfo: (id: ApodUI) -> Unit
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
        val moment = moments[position]
        if(moment.media_type=="image") {
            Glide.with(holder.momentCover)
                .load(moment.url)
                .into(holder.momentCover)
        }
        holder.itemView.setOnClickListener { v ->
            momentInfo.invoke(moment)
        }
    }

    override fun getItemCount(): Int {
        return moments.size
    }

    fun setMoments(spots: List<ApodUI>) {
        this.moments = spots
    }

    fun getMoments(): List<ApodUI> {
        return moments
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var momentCover: ImageView = view.findViewById(R.id.imageView)
    }

}
