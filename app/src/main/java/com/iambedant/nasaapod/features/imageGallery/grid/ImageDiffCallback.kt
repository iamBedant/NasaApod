package com.iambedant.nasaapod.features.imageGallery.grid

import androidx.recyclerview.widget.DiffUtil
import com.iambedant.nasaapod.data.model.ApodUI

class ImageDiffCallback(
    private val old: List<ApodUI>,
    private val new: List<ApodUI>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return old[oldPosition].date == new[newPosition].date
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return old[oldPosition] == new[newPosition]
    }

}
