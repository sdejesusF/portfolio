package sdejesus.portfolio;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import sdejesus.portfolio.data.PortfolioContract;

/**
 * Created by sdejesus on 7/2/16.
 */

public class ProjectActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private RecyclerView mListView;
    private ImageView mImageCoverView;
    private long mProjectID;

    public static final String IMAGE_COVER = "IMAGE_COVER";
    public static final String NAME_COVER = "NAME_COVER";


    private ProjectAdapter mProjectAdapter;
    private static final String SELECTED_KEY = "selected_position";

    private int mPosition = ListView.INVALID_POSITION;
    private String mCoverResource;
    private String mTitle;


    private static final int PROJECT_LOADER = 0;

    private Uri mUri;

    private static final String[] DETAIL_COLUMNS = {
            PortfolioContract.ProjectEntry.COLUMN_NAME,
            PortfolioContract.ProjectEntry.COLUMN_AVATAR,
            PortfolioContract.ProjectEntry._ID
    };

    public static final int COL_PROJECT_NAME = 0;
    public static final int COL_PROJECT_AVATAR = 1;
    public static final int COL_PROJECT_ID = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        if (getIntent() != null) {
            mUri =  getIntent().getData();
            mCoverResource = getIntent().getStringExtra(IMAGE_COVER);
            mTitle = getIntent().getStringExtra(NAME_COVER);
            Answers.getInstance().logCustom(new CustomEvent("Project  " + mTitle));
        }
        // bind views
        setGraphics();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProjectAdapter = new ProjectAdapter(getApplicationContext());

        mCollapsingToolbar.setTitle(mTitle);

        mListView.setAdapter(mProjectAdapter);
        mProjectAdapter.setOnItemClickListener(new ProjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Cursor cursor,View view) {
                if (cursor != null) {
                    Intent intent = new Intent(getApplicationContext(), ProjectDetailActivity.class);
                    Uri contentUri = PortfolioContract.ProjectEntry.buildProjectUri(cursor.getLong(COL_PROJECT_ID));
                    intent.putExtra(ProjectDetailActivity.PARAMETER_PROJECT_ID, cursor.getLong(COL_PROJECT_ID));
                    intent.setData(contentUri);
                    startActivity(intent);
                }
                mPosition = cursor.getPosition();
            }
        });

        mImageCoverView.setImageResource(this.getResources().getIdentifier(mCoverResource,"drawable",this.getPackageName()));
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        getSupportLoaderManager().initLoader(PROJECT_LOADER, null, this);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = PortfolioContract.ProjectEntry._ID + " ASC";

        if(mUri != null){
            return new CursorLoader(
                    getApplicationContext(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    sortOrder
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mProjectAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProjectAdapter.swapCursor(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void setGraphics(){
        mToolbar = (Toolbar) findViewById(R.id.projectlist_toolbar);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.projectlist_collapsing);
        mListView = (RecyclerView) findViewById(R.id.projectlist_listview);
        mImageCoverView = (ImageView) findViewById(R.id.projectlist_image);

        mListView.setLayoutManager(new GridLayoutManager(this, 2));
    }

}