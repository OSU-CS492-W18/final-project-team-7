package com.example.drake.ratecatz;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.drake.ratecatz.utils.CatUtils;
import java.util.ArrayList;

public class FavoriteCatzActivity extends AppCompatActivity
        //implements LoaderManager.LoaderCallbacks<String>,
        implements FavoriteCatzAdapter.OnPhotoItemClickedListener {

    private static final String TAG = FavoriteCatzActivity.class.getSimpleName();
    //private static final int CAT_LOADER_ID = 0;
    private static final int NUM_PHOTO_COLUMNS = 2;

    private RecyclerView mPhotosRV;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;
    private FavoriteCatzAdapter mAdapter;

    //private CatUtils.CatPhoto[] mPhotos;
    private ArrayList<CatUtils.CatPhoto> mPhotos;

    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_catz);

        //mLoadingIndicatorPB = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = (TextView)findViewById(R.id.tv_loading_error_message);
        mPhotosRV = (RecyclerView)findViewById(R.id.rv_photos);

        mAdapter = new FavoriteCatzAdapter(this);
        mPhotosRV.setAdapter(mAdapter);

        mPhotosRV.setHasFixedSize(true);
        mPhotosRV.setLayoutManager(new StaggeredGridLayoutManager(NUM_PHOTO_COLUMNS, StaggeredGridLayoutManager.VERTICAL));

        //mLoadingIndicatorPB.setVisibility(View.VISIBLE);
        //getSupportLoaderManager().initLoader(CAT_LOADER_ID, null, this);

        mPhotos = getAllFavoritedCatz();
        mAdapter.updatePhotos(mPhotos.toArray(new CatUtils.CatPhoto[mPhotos.size()]));
        for(CatUtils.CatPhoto photo : mPhotos) {
            Log.d(TAG, "Got photo: " + photo.url);
        }
    }

    /*public Loader<String> onCreateLoader(int id, Bundle args) {
        return new FavoriteCatzLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
        if(data != null) {
            mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
            mPhotosRV.setVisibility(View.VISIBLE);
            mPhotos = getAllFavoritedCatz();

            //Convert ArrayList<CatUtils.CatPhoto> to CatUtils.CatPhoto[]
            mAdapter.updatePhotos(mPhotos.toArray(new CatUtils.CatPhoto[mPhotos.size()]));
            for(CatUtils.CatPhoto photo : mPhotos) {
                Log.d(TAG, "Got photo: " + photo.url);
            }
        } else {
            mPhotosRV.setVisibility(View.INVISIBLE);
            mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        //Nothing
    }*/

    @Override
    public void onPhotoItemClicked(int photoIdx) {
        Intent intent = new Intent(this, PhotoViewActivity.class);
        intent.putExtra(PhotoViewActivity.EXTRA_PHOTOS, mPhotos);
        intent.putExtra(PhotoViewActivity.EXTRA_PHOTO_IDX, photoIdx);
        startActivity(intent);
    }

    private ArrayList<CatUtils.CatPhoto> getAllFavoritedCatz() {
        CatDBHelper dbHelper = new CatDBHelper(this);
        mDB = dbHelper.getReadableDatabase();

        Cursor cursor = mDB.query(
                CatContract.FavoritedCats.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                CatContract.FavoritedCats.COLUMN_TIMESTAMP + " DESC"
        );

        ArrayList<CatUtils.CatPhoto> allFavoritedCatz = new ArrayList<>();
        while(cursor.moveToNext()) {
            CatUtils.CatPhoto catPhoto = new CatUtils.CatPhoto();
            catPhoto.url = cursor.getString(cursor.getColumnIndex(CatContract.FavoritedCats.COLUMN_CAT_URL));
            catPhoto.id = cursor.getString(cursor.getColumnIndex(CatContract.FavoritedCats.COLUMN_CAT_ID));
            allFavoritedCatz.add(catPhoto);
        }
        cursor.close();
        return allFavoritedCatz;
    }
}
