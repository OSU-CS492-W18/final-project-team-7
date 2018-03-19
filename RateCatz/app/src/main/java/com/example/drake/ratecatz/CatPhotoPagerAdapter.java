package com.example.drake.ratecatz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.drake.ratecatz.R;
import com.example.drake.ratecatz.utils.CatUtils;

import java.util.ArrayList;

/**
 * Created by Drake on 3/18/2018.
 */

public class CatPhotoPagerAdapter extends FragmentStatePagerAdapter {

    private static ArrayList<CatUtils.CatPhoto> mPhotos;

    public CatPhotoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        if(mPhotos != null) {
            return mPhotos.size();
        } else {
            return 0;
        }
    }

    public void updatePhotos(ArrayList<CatUtils.CatPhoto> photos) {
        mPhotos = photos;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new CatPhotoFragment();
        Bundle args = new Bundle();
        args.putString(CatPhotoFragment.ARG_PHOTO_URL, mPhotos.get(position).url);
        fragment.setArguments(args);
        return fragment;
    }

    public static class CatPhotoFragment extends Fragment {
        public static final String ARG_PHOTO_URL = "photoURL";

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.photo_pager_item,
                    container, false);
            Bundle args = getArguments();
            ImageView photoIV = (ImageView) rootView.findViewById(R.id.iv_photo);
            Glide.with(photoIV.getContext())
                    .load(args.getString(ARG_PHOTO_URL))
                    .into(photoIV);
            return rootView;
        }
    }
}
