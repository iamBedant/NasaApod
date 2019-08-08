package com.iambedant.nasaapod.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.iambedant.nasaapod.R

/**
 * Created by @iamBedant on 08,August,2019
 */

fun getShimmerDrawable(context: Context): Drawable {
    val drawable = ShimmerDrawable()
    val shimmer = Shimmer.ColorHighlightBuilder().setBaseAlpha(1f)
        .setBaseColor(ContextCompat.getColor(context, R.color.colorPrimary))
        .setHighlightColor(ContextCompat.getColor(context, R.color.shimmer_color))
        .setRepeatDelay(300)
        .setHighlightAlpha(1f)
        .build()
    drawable.setShimmer(shimmer)
    return drawable
}