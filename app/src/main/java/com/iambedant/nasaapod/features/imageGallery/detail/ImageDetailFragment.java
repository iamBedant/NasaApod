package com.iambedant.nasaapod.features.imageGallery.detail;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.iambedant.nasaapod.R;
import com.iambedant.nasaapod.data.model.ApodUI;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * Created by @iamBedant on 07,August,2019
 */
public class ImageDetailFragment extends Fragment {

    private static final String EXTRA_IMAGE = "image_item";
    final RxPermissions rxPermissions = new RxPermissions(this);
    public ImageDetailFragment() {
        // Required empty public constructor
    }

    public static ImageDetailFragment newInstance(ApodUI image) {
        ImageDetailFragment fragment = new ImageDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_IMAGE, image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ApodUI image = getArguments().getParcelable(EXTRA_IMAGE);
        final PhotoView imageView = view.findViewById(R.id.detail_image);
        final TextView tvTitle = view.findViewById(R.id.tvTitle);
        final TextView tvExplaination = view.findViewById(R.id.tvExplaination);

        Glide.with(getContext())
                .load(image.getUrl())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setImageDrawable(resource);
                    }
                });

        tvTitle.setText(image.getTitle());
        tvExplaination.setText(image.getExplaination());
    }
}
