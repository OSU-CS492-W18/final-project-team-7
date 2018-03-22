package com.example.drake.ratecatz;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
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
    private OnPhotoItemLongClickedListener mOnPhotoItemLongClickedListener;


    public FavoriteCatzAdapter(OnPhotoItemClickedListener clickedListener, OnPhotoItemLongClickedListener longClickedListener) {
        mOnPhotoItemClickedListener = clickedListener;
        mOnPhotoItemLongClickedListener = longClickedListener;

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

    public interface OnPhotoItemLongClickedListener {
        void onPhotoItemLongClicked(int photoIndex);
    }

    class CatPhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView mPhotoIV;

        public CatPhotoViewHolder(View itemView) {
            super(itemView);
            mPhotoIV = itemView.findViewById(R.id.iv_cat_photo);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(CatUtils.CatPhoto photo) {
            Glide.with(mPhotoIV.getContext()).load(photo.url).into(mPhotoIV);
        }

        @Override
        public boolean onLongClick(View v) {
            //Log.v("long clicked","pos: " + getAdapterPosition());
            mOnPhotoItemLongClickedListener.onPhotoItemLongClicked(getAdapterPosition());
            return true;
        }

        @Override
        public void onClick(View view) {
            mOnPhotoItemClickedListener.onPhotoItemClicked(getAdapterPosition());
        }
    }
}
