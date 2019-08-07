package com.iambedant.nasaapod.utils

import android.view.View

/**
 * Created by @iamBedant on 08,August,2019
 */

fun View.visible(){
    this.visibility = View.VISIBLE
}

fun View.invisible(){
    this.visibility = View.INVISIBLE
}

fun View.gone(){
    this.visibility = View.GONE
}