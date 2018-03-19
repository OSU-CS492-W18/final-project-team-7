package com.example.drake.ratecatz;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;

import com.example.drake.ratecatz.utils.CatUtils;
import com.example.drake.ratecatz.utils.NetworkUtils;

import org.xml.sax.SAXException;
import com.bumptech.glide.Glide;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View.OnTouchListener;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.os.Bundle;
import android.app.Activity;

import junit.framework.Test;

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
    private ImageView mCatOverlayOneIV;
    private ImageView mCatOverlayTwoIV;
    private FrameLayout mImageFrameOne;
    private FrameLayout mImageFrameTwo;

    private ProgressBar mLoadingProgressBar;
    private TextView mLoadingErrorMessage;
    private ImageView mFavoriteCatOneButton;
    private ImageView mFavoriteCatTwoButton;
    private SQLiteDatabase mDBW;
    private SQLiteDatabase mDBR;
    private ArrayList<String> mAllFavoritedCats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingProgressBar = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessage = (TextView)findViewById(R.id.tv_loading_error_message);

        mCatPhotoOneImageView = (ImageView)findViewById(R.id.iv_cat_photo_one);
        mCatPhotoTwoImageView = (ImageView)findViewById(R.id.iv_cat_photo_two);

        mCatOverlayOneIV = (ImageView)findViewById(R.id.iv_cat_overlay_one);
        mCatOverlayTwoIV = (ImageView)findViewById(R.id.iv_cat_overlay_two);

        mImageFrameOne = (FrameLayout)findViewById(R.id.fl_image_one);
        mImageFrameTwo = (FrameLayout)findViewById(R.id.fl_image_two);

//        mCatPhotoOneImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onCatPhotoClicked();
//            }
//        });

//        mCatPhotoTwoImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onCatPhotoClicked();
//            }
//        });

        CatDBHelper dbWriteHelper = new CatDBHelper(this);
        CatDBHelper dbReadHelper = new CatDBHelper(this);
        mDBW = dbWriteHelper.getWritableDatabase();
        mDBR = dbReadHelper.getReadableDatabase();


//        mAllFavoritedCats = getAllCats();
//        Log.d(TAG, "List of favorited cats in db: \n" );
//        for (int i=0;i<mAllFavoritedCats.size();i++)
//        {
//            Log.d(TAG,mAllFavoritedCats.get(i) + " onCreate()" );
//        }

       mCatPhotoOneImageView.setOnTouchListener(new OnTouchListener() {

           @Override
           public boolean onTouch(View v, MotionEvent event) {
               Log.d("TEST", "Raw event: " + event.getAction() + ", (" + event.getRawX() + ", " + event.getRawY() + ")");
               gestureDetector.onTouchEvent(event);
               //doCatGetImageRequest();

               return true;
           }

           private GestureDetector gestureDetector = new GestureDetector(getBaseContext(), new GestureDetector.SimpleOnGestureListener() {
               @Override
               public boolean onDoubleTap(MotionEvent e) {
                   Log.d("TEST", "onDoubleTap");
                   onCatFavorite(0);
                   return super.onDoubleTap(e);
               }

               @Override
               public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                   onCatPhotoClicked(0, velocityX);
                   return super.onFling(e1, e2, velocityX, velocityY);
               }

               @Override
               public boolean onSingleTapUp(MotionEvent e) {
                   return super.onSingleTapUp(e);
               }

               @Override
               public boolean onSingleTapConfirmed(MotionEvent e) {
                 //  onCatPhotoClicked();
                   return super.onSingleTapConfirmed(e);
               }
           });

       });


        mCatPhotoTwoImageView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("TEST", "Raw event: " + event.getAction() + ", (" + event.getRawX() + ", " + event.getRawY() + ")");
                gestureDetector.onTouchEvent(event);
                
                return true;
            }

            private GestureDetector gestureDetector = new GestureDetector(getBaseContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Log.d("TEST", "onDoubleTap");
                    onCatFavorite(1);
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    onCatPhotoClicked(1, velocityX);
                    return super.onFling(e1, e2, velocityX, velocityY);
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return super.onSingleTapUp(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    //
                    return super.onSingleTapConfirmed(e);
                }
            });

        });



    }

/*
    public void onCatPhotoClicked() {
        doCatGetImageRequest();
        /*String catImageUrl = CatUtils.buildGetCatImagesURL();
        Log.d(TAG, "doCatImageRequest building another URL: " + catImageUrl);
        new CatImageFetchTask().execute(catImageUrl);
        */

    public void onCatPhotoClicked(int photoID, float velocity) {
        doCatGetImageRequest();
        ImageView imageChosen = (photoID == 0)?mCatPhotoOneImageView:mCatPhotoTwoImageView;
        ImageView overlayChosen = (photoID == 0)?mCatOverlayOneIV:mCatOverlayTwoIV;
        float distance = (velocity > 0)?1400f:-1400f;

        final AnimatorSet animatorSet = new AnimatorSet();


        ObjectAnimator animation = ObjectAnimator.ofFloat(imageChosen, "translationX", distance);
        animation.setDuration(140);
        ObjectAnimator animationOverlay = ObjectAnimator.ofFloat(overlayChosen, "translationX", distance);
        animationOverlay.setDuration(140);
        animatorSet.play(animation).with(animationOverlay);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCatOverlayOneIV.setVisibility(View.INVISIBLE);
                mCatOverlayTwoIV.setVisibility(View.INVISIBLE);
                mCatPhotoOneImageView.clearAnimation();
                mCatPhotoTwoImageView.clearAnimation();
                mCatOverlayOneIV.clearAnimation();
                mCatOverlayTwoIV.clearAnimation();

                animation.removeListener(this);
                animation.setDuration(0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ((AnimatorSet) animation).reverse();
                }
                animation.cancel();


            }
        });
        animatorSet.start();
        //doCatGetImageRequest();
    }

    public void onCatFavorite(int photoID){
        ImageView catImageView = (photoID == 0)?mCatPhotoOneImageView:mCatPhotoTwoImageView;
        ImageView catOverlay = (photoID == 0)?mCatOverlayOneIV:mCatOverlayTwoIV;

        if(!checkIfInFavorites(mCatPhotos.get(photoID).id)) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_favorite_overlay);
            Toast.makeText(catImageView.getContext(), "Added " + (photoID==0?"first":"second") + " cat to favorites", Toast.LENGTH_SHORT).show();
            addCatToDB(mCatPhotos.get(photoID));

            catOverlay.setMinimumWidth(catImageView.getWidth());
            catOverlay.setVisibility(View.VISIBLE);
            catOverlay.setImageDrawable(drawable);
            catOverlay.bringToFront();

        } else {
            Toast.makeText(catImageView.getContext(), "Removed " + (photoID==0?"first":"second") + " cat from favorites", Toast.LENGTH_SHORT).show();
            deleteCatFromFavorites(mCatPhotos.get(photoID).id);

            catOverlay.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        mDBR.close();
        mDBW.close();
        super.onDestroy();
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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        String pref_tag;
        pref_tag= sharedPreferences.getString("pref_tag", "none");


        mCatOverlayOneIV.setVisibility(View.INVISIBLE);
        mCatOverlayTwoIV.setVisibility(View.INVISIBLE);

        String catImageUrl = CatUtils.buildGetCatImagesURL();
        new CatImageFetchTask().execute((catImageUrl));
        Log.d(TAG, "doCatImageRequest building URL: " + catImageUrl);
        Log.d(TAG, "pref_tag=" + pref_tag);


       if (pref_tag.equals("none")){
            new CatImageFetchTask().execute((catImageUrl));
            Log.d(TAG, "doCatImageRequest building URL: " + catImageUrl);
        } else{
            catImageUrl = catImageUrl + "&category=" + pref_tag;
            Log.d(TAG, "doCatImageRequest building URL2: " + catImageUrl);
            new CatImageFetchTask().execute((catImageUrl));
        }

    }

    public class CatImageFetchTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mLoadingProgressBar.setVisibility(View.VISIBLE);
            mCatPhotoTwoImageView.setVisibility(View.INVISIBLE);
            mCatPhotoOneImageView.setVisibility(View.INVISIBLE);
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
            mLoadingProgressBar.setVisibility(View.GONE);
            mCatPhotoOneImageView.setVisibility(View.VISIBLE);
            mCatPhotoTwoImageView.setVisibility(View.VISIBLE);

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

    private void deleteCatFromFavorites(String id) {
        if(id != null) {
            String sqlSelection = CatContract.FavoritedCats.COLUMN_CAT_ID + " = ?";
            String[] sqlSelectionArgs = {id};
            mDBW.delete(CatContract.FavoritedCats.TABLE_NAME, sqlSelection, sqlSelectionArgs);
        }
    }

    private long addCatToDB(CatUtils.CatPhoto cat) {
        if( cat != null) {
            ContentValues values = new ContentValues();
            values.put(CatContract.FavoritedCats.COLUMN_CAT_URL, cat.url);
            values.put(CatContract.FavoritedCats.COLUMN_CAT_ID, cat.id);
            return mDBW.insert(CatContract.FavoritedCats.TABLE_NAME,null, values);
        } else {
            return -1;
        }
    }

    private boolean checkIfInFavorites(String id) {
        boolean isDuplicate = false;
        if(id != null) {
            String sqlSelection =
                    CatContract.FavoritedCats.COLUMN_CAT_ID + " = ?";
            String[] sqlSelectionArgs = {id};
            Cursor cursor = mDBR.query(
                    CatContract.FavoritedCats.TABLE_NAME,
                    null,
                    sqlSelection,
                    sqlSelectionArgs,
                    null,
                    null,
                    null
            );

            isDuplicate = cursor.getCount() > 0;
            cursor.close();
        }
        return isDuplicate;
    }

    private ArrayList<String> getAllCats(){
        Cursor cursor = mDBR.query(
                CatContract.FavoritedCats.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                CatContract.FavoritedCats.COLUMN_TIMESTAMP + " DESC"
        );
        ArrayList<String> favoriteCatList = new ArrayList<>();

        cursor.moveToFirst();
        do{
            String url = cursor.getString(cursor.getColumnIndex(CatContract.FavoritedCats.COLUMN_CAT_URL));
            favoriteCatList.add(url);
        }while (cursor.moveToNext());

        cursor.close();
        return favoriteCatList;
    }
}
