package com.example.drake.ratecatz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;

import com.example.drake.ratecatz.utils.CatUtils;
import com.example.drake.ratecatz.utils.NetworkUtils;

import org.xml.sax.SAXException;
import com.bumptech.glide.Glide;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<String>{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CAT_LOADER_ID = 1;
    private static final String CAT_URL_KEY = "catUrl";
    private static final int NUM_PHOTO_COLUMNS = 2;
    private ImageView mCatPhotoOneImageView;
    private ImageView mCatPhotoTwoImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportLoaderManager().initLoader(CAT_LOADER_ID, null, this);
        doCatGetImageRequest();

          mCatPhotoOneImageView = (ImageView)findViewById(R.id.iv_cat_photo_one);
          mCatPhotoTwoImageView = (ImageView)findViewById(R.id.iv_cat_photo_two);

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

        Bundle argsBundle = new Bundle();
        argsBundle.putString(CAT_URL_KEY, catImageUrl);
        getSupportLoaderManager().restartLoader(CAT_LOADER_ID, argsBundle, this);

    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public android.support.v4.content.Loader<String> onCreateLoader(int id, final Bundle args) {
        return new android.support.v4.content.AsyncTaskLoader<String>(this) {

            String mCatResultsXML;

            @Override
            protected void onStartLoading() {
                if(args != null) {
                    if (mCatResultsXML != null) {
                        deliverResult(mCatResultsXML);
                    } else {
                        forceLoad();
                    }
                }

            }
            @Override
            public String loadInBackground() {
                 if(args != null) {
                     String catUrl = args.getString(CAT_URL_KEY);
                     Log.d(TAG, "AsyncTaskLoader making network call: " + catUrl);
                     String catResult = null;
                     try {
                         catResult = NetworkUtils.doHTTPGet(catUrl);
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                     return catResult;
                 } else {
                     return null;
                 }
            }

            @Override
            public void deliverResult(String data) {
                mCatResultsXML = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<String> loader, String data) {
        Log.d(TAG, "AsyncTaskLoader's onLoadFinished called");
        if(data != null) {
            try {
                ArrayList<CatUtils.CatPhoto> catPhotoList = new ArrayList<CatUtils.CatPhoto>();
                catPhotoList = CatUtils.parseCatAPIGetImageResultXML(data);

                CatUtils.CatPhoto catPhoto1 = new CatUtils.CatPhoto();
                CatUtils.CatPhoto catPhoto2 = new CatUtils.CatPhoto();

                catPhoto1 = catPhotoList.get(0);
                catPhoto2 = catPhotoList.get(1);

                Glide.with(mCatPhotoOneImageView.getContext())
                .load(catPhoto1.url)
                .into(mCatPhotoOneImageView);

                Glide.with(mCatPhotoTwoImageView.getContext())
                        .load(catPhoto2.url)
                        .into(mCatPhotoTwoImageView);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<String> loader) {
        // nothing to do ...
    }
}
