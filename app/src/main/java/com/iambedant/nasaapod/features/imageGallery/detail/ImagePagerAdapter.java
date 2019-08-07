package com.iambedant.nasaapod.features.imageGallery.detail;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.iambedant.nasaapod.data.model.ApodUI;

import java.util.List;

public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    private List<ApodUI> images;

    public ImagePagerAdapter(FragmentManager fm, List<ApodUI> images) {
        super(fm);
        this.images = images;
    }

    @Override
    public Fragment getItem(int position) {
        ApodUI image = images.get(position);
        return ImageDetailFragment.newInstance(image);
    }

    @Override
    public int getCount() {
        return images.size();
    }
}