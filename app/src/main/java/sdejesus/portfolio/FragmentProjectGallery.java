package sdejesus.portfolio;

import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import sdejesus.portfolio.data.PortfolioContract;

/**
 * Created by sdejesus on 7/9/16.
 */

public class FragmentProjectGallery extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = FragmentProjectGallery.class.getSimpleName();

    private static final String SELECTED_KEY = "selected_position";

    private int mPosition = ListView.INVALID_POSITION;
    private static final int PROJECT_GALLERY_LOADER = 0;

    private ProjectGalleryAdapter mProjectGalleryAdapter;
    private RecyclerView mListView;
    private Uri mUri;
    private long mProjectId;


    private static final String[] DETAIL_COLUMNS = {
            PortfolioContract.ProjectGalleryEntry.COLUMN_AVATAR,
            PortfolioContract.ProjectGalleryEntry.COLUMN_DESCRIPTION,
            PortfolioContract.ProjectGalleryEntry.COLUMN_PROJECT_KEY,
            PortfolioContract.ProjectGalleryEntry._ID
    };

    public static final int COL_PROJECT_GALLERY_AVATAR = 0;
    public static final int COL_PROJECT_GALLERY_DESCRIPTION = 1;
    public static final int COL_PROJECT_GALLERY_PROJECT_KEY = 2;
    public static final int COL_PROJECT_GALLERY_ID = 3;


    public FragmentProjectGallery() {
        // Required empty public constructor
    }
    private final ContentObserver CONTENT_OBSERVER = new ContentObserver(new Handler()){
        @Override
        public void onChange(boolean selfChange) {
            getLoaderManager().restartLoader(PROJECT_GALLERY_LOADER,null,FragmentProjectGallery.this);
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        getActivity().getContentResolver().registerContentObserver(PortfolioContract.ProjectGalleryEntry.CONTENT_URI,true, CONTENT_OBSERVER);

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getContentResolver().unregisterContentObserver(CONTENT_OBSERVER);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle arguments = getArguments();

        if (arguments != null) {
            mProjectId = arguments.getLong(ProjectDetailActivity.PARAMETER_PROJECT_ID);
            mUri = PortfolioContract.ProjectGalleryEntry.buildProjectGalleryUri(mProjectId);
        }

        mProjectGalleryAdapter = new ProjectGalleryAdapter(getActivity());

        View rootView = inflater.inflate(R.layout.fragment_projectgallery, container, false);

        setGraphics(rootView);
        // Get a reference to the ListView, and attach this adapter to it.
        mListView.setAdapter(mProjectGalleryAdapter);

        mProjectGalleryAdapter.setOnItemClickListener(new ProjectGalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Cursor cursor,View view) {
                animateIntent(view,cursor);
            }
        });
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        return rootView;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(PROJECT_GALLERY_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = PortfolioContract.ProjectGalleryEntry._ID + " ASC";

        return new CursorLoader(getActivity(),
                mUri,
                DETAIL_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mProjectGalleryAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProjectGalleryAdapter.swapCursor(null);
    }

    public void setGraphics(View rootView){
        mListView = (RecyclerView) rootView.findViewById(R.id.projectgallery_listview);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mListView.setLayoutManager(layoutManager);
    }
    public void animateIntent(View view,Cursor cursor) {

        Intent intent = new Intent(getContext(), ImageFullActivity.class);
        ImageView image = (ImageView)view.findViewById(R.id.item_project_gallery_image);
        intent.putExtra(ImageFullActivity.PARAMETER_IMAGE,cursor.getString(COL_PROJECT_GALLERY_AVATAR));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), image, getString(R.string.transition_string));
            startActivity(intent, options.toBundle());
        }
        else {
            startActivity(intent);
        }
   }
}