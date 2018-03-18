package com.example.drake.ratecatz;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.drake.ratecatz.utils.CatUtils;
import com.example.drake.ratecatz.utils.NetworkUtils;

import java.io.IOException;

/**
 * Created by Drake on 3/17/2018.
 */

public class FavoriteCatzLoader extends AsyncTaskLoader<String> {

    private final static String TAG = FavoriteCatzLoader.class.getSimpleName();

    String mResultsXML;

    FavoriteCatzLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if(mResultsXML != null) {
            deliverResult(mResultsXML);
        } else {
            forceLoad();
        }
    }

    @Override
    public String loadInBackground() {
        String catURL = CatUtils.buildGetCatImagesURL();
        String results = null;
        try {
            results = NetworkUtils.doHTTPGet(catURL);
        } catch (IOException e) {
            Log.d(TAG, "Error connecting to Cat API", e);
        }
        return results;
    }

    @Override
    public void deliverResult(String data) {
        mResultsXML = data;
        super.deliverResult(data);
    }
}
