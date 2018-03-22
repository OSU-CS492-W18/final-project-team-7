package com.example.drake.ratecatz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drake.ratecatz.utils.CatUtils;
import java.util.ArrayList;

public class FavoriteCatzActivity extends AppCompatActivity
        implements FavoriteCatzAdapter.OnPhotoItemClickedListener, FavoriteCatzAdapter.OnPhotoItemLongClickedListener {

    private static final String TAG = FavoriteCatzActivity.class.getSimpleName();
    private static final int NUM_PHOTO_COLUMNS = 2;

    private RecyclerView mPhotosRV;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;
    private FavoriteCatzAdapter mAdapter;

    private ArrayList<CatUtils.CatPhoto> mPhotos;

    private SQLiteDatabase mDB;
    String mDeleteCatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_catz);

        mLoadingErrorMessageTV = (TextView)findViewById(R.id.tv_loading_error_message);
        mPhotosRV = (RecyclerView)findViewById(R.id.rv_photos);

        mAdapter = new FavoriteCatzAdapter(this, this);
        mPhotosRV.setAdapter(mAdapter);

        mPhotosRV.setHasFixedSize(true);
        mPhotosRV.setLayoutManager(new StaggeredGridLayoutManager(NUM_PHOTO_COLUMNS, StaggeredGridLayoutManager.VERTICAL));

        printPhotos();
    }

    private void printPhotos() {
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

        //Delete photo
        if(mDeleteCatId != null) {
            String sqlSelection = CatContract.FavoritedCats.COLUMN_CAT_ID + " = ?";
            String[] sqlSelectionArgs = {mDeleteCatId};
            mDB.delete(CatContract.FavoritedCats.TABLE_NAME, sqlSelection, sqlSelectionArgs);
        }
        Toast.makeText(this, "Removed cat from favorites ", Toast.LENGTH_SHORT).show();

        //Print updated list
        printPhotos();
    }

    @Override
    public void onPhotoItemLongClicked(int photo) {
        showDialog(this, "Are you sure you want to delete such a pretty kitty?");
        mDeleteCatId = mPhotos.get(photo).id;
    }

    public void showDialog(Activity activity, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if(title != null)
            builder.setTitle(title);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCatFromFavorites();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
