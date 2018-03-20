package com.example.drake.ratecatz;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.drake.ratecatz.utils.CatUtils;
import com.example.drake.ratecatz.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Nathan on 3/19/18.
 */

public class MainActivityLoader extends AsyncTaskLoader<String> {

    private final static String TAG = MainActivityLoader.class.getSimpleName();

    private String mURLFetch;
    private String mCachedXML;

    public MainActivityLoader(Context context, String url) {
        super(context);
        mURLFetch = url;
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "Loader started");
        if (mURLFetch != null) {
            if (mCachedXML != null) {
                Log.d(TAG, "Returning cached xml");
                deliverResult(mCachedXML);
            } else {
                Log.d(TAG, "Force loaded");
                forceLoad();
            }
        }
    }

    @Override
    public String loadInBackground() {
        String catXML = null;
        if (mURLFetch != null) {
            try{
                catXML = NetworkUtils.doHTTPGet(mURLFetch);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return catXML;
    }

    @Override
    public void deliverResult(@Nullable String data) {
        mCachedXML = data;
        super.deliverResult(data);
    }
}
