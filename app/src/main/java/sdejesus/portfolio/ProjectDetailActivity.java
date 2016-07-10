package sdejesus.portfolio;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import sdejesus.portfolio.data.PortfolioContract;

/**
 * Created by sdejesus on 7/5/16.
 */

public class ProjectDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private TextView mCompany;
    private TextView mDescriptionView;
    private TextView mLinkView;
    private TextView mRepositoryView;
    private TextView mTagsView;
    private TextView mYearView;

    public final static String PARAMETER_PROJECT_ID = "PROJECTID";
    private static final int PROJECT_LOADER = 0;

    private long mProjectID;
    private Uri mUri;

    private static final String[] DETAIL_COLUMNS = {
        PortfolioContract.ProjectEntry.COLUMN_NAME,
        PortfolioContract.ProjectEntry.COLUMN_AVATAR,
        PortfolioContract.ProjectEntry.COLUMN_YEAR,
        PortfolioContract.ProjectEntry.COLUMN_COMPANY,
        PortfolioContract.ProjectEntry.COLUMN_LINK,
        PortfolioContract.ProjectEntry.COLUMN_REPOSITORY,
        PortfolioContract.ProjectEntry.COLUMN_TAGS,
        PortfolioContract.ProjectEntry.COLUMN_DESCRIPTION,
        PortfolioContract.ProjectEntry._ID
    };

    public static final int COL_PROJECT_NAME = 0;
    public static final int COL_PROJECT_AVATAR = 1;
    public static final int COL_PROJECT_YEAR = 2;
    public static final int COL_PROJECT_COMPANY = 3;
    public static final int COL_PROJECT_LINK = 4;
    public static final int COL_PROJECT_REPOSITORY = 5;
    public static final int COL_PROJECT_TAGS = 6;
    public static final int COL_PROJECT_DESCRIPTION = 7;
    public static final int COL_PROJECT_ID = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        // bind views
        setGraphics();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().getData() != null){
            mUri = getIntent().getData();
            mProjectID = getIntent().getLongExtra(PARAMETER_PROJECT_ID,0);
            Bundle arguments = new Bundle();
            arguments.putLong(PARAMETER_PROJECT_ID,mProjectID);
            FragmentProjectGallery fragmentProjectGallery = new FragmentProjectGallery();
            fragmentProjectGallery.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.projectdetail_fragmentlayout_gallery,fragmentProjectGallery).commit();
        }


        getSupportLoaderManager().initLoader(PROJECT_LOADER, null, this);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if(mUri != null){
            return new CursorLoader(
                    getApplicationContext(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            //int drawableResourceId = this.getResources().getIdentifier(data.getString(COL_PROJECT_AVATAR), "drawable", this.getPackageName());
            mCollapsingToolbar.setTitle(data.getString(COL_PROJECT_NAME));
            mLinkView.setText(data.getString(COL_PROJECT_LINK));
            mTagsView.setText(data.getString(COL_PROJECT_TAGS));
            mCompany.setText(data.getString(COL_PROJECT_COMPANY));
            mDescriptionView.setText(data.getString(COL_PROJECT_DESCRIPTION));
            mRepositoryView.setText(data.getString(COL_PROJECT_REPOSITORY));
            mYearView.setText(data.getString(COL_PROJECT_YEAR));
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    public void setGraphics(){
        mToolbar = (Toolbar) findViewById(R.id.projectdetail_toolbar);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.projectdetail_collapsing);
        mLinkView = (TextView) findViewById(R.id.projectdetail_textview_link);
        mTagsView = (TextView) findViewById(R.id.projectdetail_textview_tags);
        mCompany = (TextView) findViewById(R.id.projectdetail_textview_company);
        mRepositoryView = (TextView) findViewById(R.id.projectdetail_textview_repository);
        mDescriptionView = (TextView) findViewById(R.id.projectdetail_textview_description);
        mYearView = (TextView) findViewById(R.id.projectdetail_textview_year);
   }
}