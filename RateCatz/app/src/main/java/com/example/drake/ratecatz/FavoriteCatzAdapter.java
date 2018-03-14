package com.example.drake.ratecatz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.drake.ratecatz.utils.CatUtils;

/**
 * Created by aleaweeks on 3/12/18.
 */

public class FavoriteCatzAdapter extends FragmentStatePagerAdapter {

    CatUtils.CatPhoto[] mCatPhotos;

    public FavoriteCatzAdapter(FragmentManager fm) {
        super(fm);
    }

    public void updatePhotos(CatUtils.CatPhoto[] catPhotos) {
        mCatPhotos = catPhotos;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new CatPhotoFragment();
        Bundle args = new Bundle();
        args.putString(CatPhotoFragment.ARGS_PHOTO_URL, mCatPhotos[position].url);
        fragment.setArguments(args);
        return null;
    }

    @Override
    public int getCount() {
        if(mCatPhotos != null){
            return mCatPhotos.length;
        } else {
            return 0;
        }
    }

    public static class CatPhotoFragment extends Fragment {
        public static final String ARGS_PHOTO_URL = "catUrl";

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.cat_photo_pager_item, container, false);
            Bundle args = getArguments();
            ImageView photoIV = (ImageView)rootView.findViewById(R.id.iv_photo);
            String photoUrl = args.getString(ARGS_PHOTO_URL);
            Glide.with(photoIV.getContext())
                    .load(photoUrl)
                    .into(photoIV);
            return rootView;
        }
    }
}
