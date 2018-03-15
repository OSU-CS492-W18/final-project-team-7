package com.example.drake.ratecatz;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;

import com.example.drake.ratecatz.utils.CatUtils;
import com.example.drake.ratecatz.utils.NetworkUtils;

import org.xml.sax.SAXException;
import com.bumptech.glide.Glide;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CAT_LOADER_ID = 1;
    private static final String CAT_URL_KEY = "catUrl";
    private static final int NUM_PHOTO_COLUMNS = 2;
    private ArrayList<CatUtils.CatPhoto> mCatPhotos;
    private ImageView mCatPhotoOneImageView;
    private ImageView mCatPhotoTwoImageView;
    private ProgressBar mLoadingProgressBar;
    private TextView mLoadingErrorMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingProgressBar = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessage = (TextView)findViewById(R.id.tv_loading_error_message);

        mCatPhotoOneImageView = (ImageView)findViewById(R.id.iv_cat_photo_one);
        mCatPhotoTwoImageView = (ImageView)findViewById(R.id.iv_cat_photo_two);

        mCatPhotoOneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCatPhotoClicked();
            }
        });

        mCatPhotoTwoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCatPhotoClicked();
            }
        });

        doCatGetImageRequest();



    }

    public void onCatPhotoClicked() {
        String catImageUrl = CatUtils.buildGetCatImagesURL();
        Log.d(TAG, "doCatImageRequest building another URL: " + catImageUrl);
        new CatImageFetchTask().execute(catImageUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final Intent favoritesActivityIntent = new Intent(this, FavoriteCatzActivity.class);
        final Intent settingsActivityIntent = new Intent(this, SettingsActivity.class);

        switch (item.getItemId()) {
            case R.id.action_favorites:
                startActivity(favoritesActivityIntent);
                return true;
            case R.id.action_settings:
                startActivity(settingsActivityIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void doCatGetImageRequest() {
        String catImageUrl = CatUtils.buildGetCatImagesURL();
        Log.d(TAG, "doCatImageRequest building URL: " + catImageUrl);
        new CatImageFetchTask().execute(catImageUrl);
    }

    public class CatImageFetchTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mLoadingProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String catRequestURL = strings[0];

            String catResults = null;

            try{
                catResults = NetworkUtils.doHTTPGet(catRequestURL);
            } catch(IOException e) {
                e.printStackTrace();
            }
            return catResults;
        }

        @Override
        protected void onPostExecute(String s) {
            mLoadingProgressBar.setVisibility(View.INVISIBLE);
            if(s != null) {
                try {
                    mCatPhotos = CatUtils.parseCatAPIGetImageResultXML(s);
                    CatUtils.CatPhoto catPhoto1 = new CatUtils.CatPhoto();
                    CatUtils.CatPhoto catPhoto2 = new CatUtils.CatPhoto();

                    catPhoto1 = mCatPhotos.get(0);
                    catPhoto2 = mCatPhotos.get(1);

                    Glide.with(mCatPhotoOneImageView.getContext())
                    .load(catPhoto1.url)
                    .into(mCatPhotoOneImageView);

                    Glide.with(mCatPhotoTwoImageView.getContext())
                            .load(catPhoto2.url)
                            .into(mCatPhotoTwoImageView);

                    for(CatUtils.CatPhoto photo : mCatPhotos) {
                        Log.d(TAG, "Got photo" + photo.url);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }

            } else {
                mLoadingErrorMessage.setVisibility(View.VISIBLE);
            }
        }
    }
}
