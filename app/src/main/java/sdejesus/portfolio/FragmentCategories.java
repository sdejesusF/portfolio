package sdejesus.portfolio;

import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import sdejesus.portfolio.data.PortfolioContract;

/**
 * Created by sdejesus on 7/2/16.
 */

public class FragmentCategories extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = FragmentCategories.class.getSimpleName();

    private static final String SELECTED_KEY = "selected_position";

    private int mPosition = ListView.INVALID_POSITION;
    private static final int CATEGORY_LOADER = 0;

    private CategoryAdapter mCategoryAdapter;
    private long mUserId;
    private RecyclerView mListView;

    private static final String[] DETAIL_COLUMNS = {
        PortfolioContract.CategoryEntry.COLUMN_NAME,
        PortfolioContract.CategoryEntry.COLUMN_DESCRIPTION,
        PortfolioContract.CategoryEntry.COLUMN_AVATAR,
        PortfolioContract.CategoryEntry._ID
    };

    public static final int COL_CATEGORY_NAME = 0;
    public static final int COL_CATEGORY_DESCRIPTION = 1;
    public static final int COL_CATEGORY_AVATAR = 2;
    public static final int COL_CATEGORY_ID = 3;


    public FragmentCategories() {
        // Required empty public constructor
    }
    private final ContentObserver CONTENT_OBSERVER = new ContentObserver(new Handler()){
        @Override
        public void onChange(boolean selfChange) {
            getLoaderManager().restartLoader(CATEGORY_LOADER,null,FragmentCategories.this);
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        getActivity().getContentResolver().registerContentObserver(PortfolioContract.CategoryEntry.CONTENT_URI,true, CONTENT_OBSERVER);

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
            mUserId = arguments.getLong(MainActivity.USER_ID_PARAMETER);
        }

        mCategoryAdapter = new CategoryAdapter(getActivity());

        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        setGraphics(rootView);
        // Get a reference to the ListView, and attach this adapter to it.
        mListView.setAdapter(mCategoryAdapter);

        mCategoryAdapter.setOnItemClickListener(new SkillAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Cursor cursor) {
                if (cursor != null) {
                    Intent intent = new Intent(getContext(), ProjectActivity.class);
                    Uri contentUri = PortfolioContract.ProjectEntry.buildProjectByCategoryAndUser(cursor.getLong(COL_CATEGORY_ID),mUserId);
                    intent.setData(contentUri);
                    intent.putExtra(ProjectActivity.IMAGE_COVER,cursor.getString(COL_CATEGORY_AVATAR));
                    intent.putExtra(ProjectActivity.NAME_COVER,cursor.getString(COL_CATEGORY_NAME));
                    startActivity(intent);
                }
                mPosition = cursor.getPosition();
            }
        });

        // If there's instance state, mine it for useful information.
        // The end-goal here is that the user never knows that turning their device sideways
        // does crazy lifecycle related things.  It should feel like some stuff stretched out,
        // or magically appeared to take advantage of room, but data or place in the app was never
        // actually *lost*.
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        return rootView;
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
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CATEGORY_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = PortfolioContract.CategoryEntry._ID + " ASC";

        Uri categoryUri = PortfolioContract.CategoryEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                categoryUri,
                DETAIL_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCategoryAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCategoryAdapter.swapCursor(null);
    }

    public void setGraphics(View rootView){
        mListView = (RecyclerView) rootView.findViewById(R.id.category_listview);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mListView.setLayoutManager(layoutManager);
    }
}