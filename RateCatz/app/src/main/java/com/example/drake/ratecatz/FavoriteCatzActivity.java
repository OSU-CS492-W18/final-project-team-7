package com.example.drake.ratecatz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drake.ratecatz.utils.CatUtils;
import java.util.ArrayList;

public class FavoriteCatzActivity extends AppCompatActivity
        //implements LoaderManager.LoaderCallbacks<String>,
        implements FavoriteCatzAdapter.OnPhotoItemClickedListener, FavoriteCatzAdapter.OnPhotoItemLongClickedListener {

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
    //private boolean mDeleteCat = false;
    String mDeleteCatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_catz);

        //mLoadingIndicatorPB = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = (TextView)findViewById(R.id.tv_loading_error_message);
        mPhotosRV = (RecyclerView)findViewById(R.id.rv_photos);

        mAdapter = new FavoriteCatzAdapter(this, this);
        mPhotosRV.setAdapter(mAdapter);

        mPhotosRV.setHasFixedSize(true);
        mPhotosRV.setLayoutManager(new StaggeredGridLayoutManager(NUM_PHOTO_COLUMNS, StaggeredGridLayoutManager.VERTICAL));

        mPhotos = getAllFavoritedCatz();
        mAdapter.updatePhotos(mPhotos.toArray(new CatUtils.CatPhoto[mPhotos.size()]));
        for(CatUtils.CatPhoto photo : mPhotos) {
            Log.d(TAG, "Got photo: " + photo.url);
        }
    }

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

    public void deleteCatFromFavorites() {
        CatDBHelper dbHelper = new CatDBHelper(this);
        mDB = dbHelper.getReadableDatabase();
        if(mDeleteCatId != null) {
            String sqlSelection = CatContract.FavoritedCats.COLUMN_CAT_ID + " = ?";
            String[] sqlSelectionArgs = {mDeleteCatId};
            mDB.delete(CatContract.FavoritedCats.TABLE_NAME, sqlSelection, sqlSelectionArgs);
        }

        Toast.makeText(this, "Removed cat from favorites ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPhotoItemLongClicked(int photo) {
        //Log.d("test", "LOG: mDeleteCat = " + mDeleteCat);
        showDialog(this, "Are you sure you want to delete such a pretty kitty?");
        //Log.d("test", "LOG: mDeleteCat = " + mDeleteCat);
        mDeleteCatId = mPhotos.get(photo).id;
        /*if(mDeleteCat) {
            Log.d("test", "LOG: in if statement");
            Toast.makeText(this, "Removed cat from favorites ", Toast.LENGTH_SHORT).show();
            deleteCatFromFavorites(mPhotos.get(photo).id);
            mDeleteCat = false;
        }*/
        Log.d("test", "LOG: after if statement");
    }

    public void showDialog(Activity activity, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if(title != null)
            builder.setTitle(title);

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //mDeleteCat = true;
                deleteCatFromFavorites();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
