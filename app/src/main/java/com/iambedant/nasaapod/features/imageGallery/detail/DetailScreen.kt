package com.iambedant.nasaapod.features.imageGallery.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.iambedant.nasaapod.R
import com.iambedant.nasaapod.features.imageGallery.CurrentData
import kotlinx.android.synthetic.main.activity_detail_screen.*

class DetailScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_screen)

        val galleryPagerAdapter = ImagePagerAdapter(supportFragmentManager, CurrentData.model!!.listOfImages)
        viewPager.adapter = galleryPagerAdapter
        viewPager.currentItem = CurrentData.selectedPosition

        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                CurrentData.currentPosition = position
            }
        })
        ivBack.setOnClickListener { finish() }
    }
}
