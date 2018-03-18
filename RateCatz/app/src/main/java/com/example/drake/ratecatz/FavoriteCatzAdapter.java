package com.example.drake.ratecatz;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.drake.ratecatz.utils.CatUtils;

/**
 * Created by aleaweeks on 3/12/18.
 */

public class FavoriteCatzAdapter extends RecyclerView.Adapter<FavoriteCatzAdapter.CatPhotoViewHolder> {

    private CatUtils.CatPhoto[] mPhotos;
    private OnPhotoItemClickedListener mOnPhotoItemClickedListener;

    public FavoriteCatzAdapter(OnPhotoItemClickedListener clickedListener) {
        mOnPhotoItemClickedListener = clickedListener;
    }

    public void updatePhotos(CatUtils.CatPhoto[] photos) {
        mPhotos = photos;
        notifyDataSetChanged();
    }

    @Override
    public CatPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.favorite_cat_grid_item, parent, false);
        return new CatPhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CatPhotoViewHolder holder, int position) {
        holder.bind(mPhotos[position]);
    }

    @Override
    public int getItemCount() {
        if (mPhotos != null) {
            return mPhotos.length;
        } else {
            return 0;
        }
    }

    public interface OnPhotoItemClickedListener {
        void onPhotoItemClicked(int photoIdx);
    }

    class CatPhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mPhotoIV;

        public CatPhotoViewHolder(View itemView) {
            super(itemView);
            mPhotoIV = itemView.findViewById(R.id.iv_cat_photo); //TODO verify this is correct
            itemView.setOnClickListener(this);
        }

        public void bind(CatUtils.CatPhoto photo) {
            Glide.with(mPhotoIV.getContext()).load(photo.url).into(mPhotoIV);

            //Rob's example with place holder:
            /*
            Glide.with(mPhotoIV.getContext())
                    .load(photo.url_m)
                    .apply(RequestOptions.placeholderOf(new SizedColorDrawable(Color.WHITE, photo.width_m, photo.height_m)))
                    .into(mPhotoIV);
            */
        }

        @Override
        public void onClick(View view) {
            mOnPhotoItemClickedListener.onPhotoItemClicked(getAdapterPosition());
        }
    }

    //Class used with place holder
    /*
    class SizedColorDrawable extends ColorDrawable {
        int mWidth = -1;
        int mHeight = -1;

        public SizedColorDrawable(int color, int width, int height) {
            super(color);
            mWidth = width;
            mHeight = height;
        }

        @Override
        public int getIntrinsicWidth() {
            return mWidth;
        }

        @Override
        public int getIntrinsicHeight() {
            return mHeight;
        }
    }
    */
}
