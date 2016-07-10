package sdejesus.portfolio;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import sdejesus.portfolio.data.PortfolioContract;

/**
 * Created by sdejesus on 7/6/16.
 */

public class FragmentSkills extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mUri;
    private long mUserId;
    private RecyclerView mListView;
    private SkillAdapter mActivityAdapter;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";


    private static final int USER_SKILLS_LOADER = 0;

    private static final String[] DETAIL_COLUMNS = {
            PortfolioContract.UserSkillsEntry.COLUMN_NAME,
            PortfolioContract.UserSkillsEntry.COLUMN_DESCRIPTION,
            PortfolioContract.UserSkillsEntry.COLUMN_AVATAR,
            PortfolioContract.UserSkillsEntry.COLUMN_USER_KEY,
            PortfolioContract.UserSkillsEntry._ID

    };

    public static final int COL_USER_ACTIVITIES_NAME = 0;
    public static final int COL_USER_ACTIVITIES_DESCRIPTION = 1;
    public static final int COL_USER_ACTIVITIES_AVATAR = 2;
    public static final int COL_USER_ACTIVITIES_USER_ID = 3;
    public static final int COL_USER_ACTIVITIES_ID = 4;


    public FragmentSkills() {
        // Required empty public constructor
    }
    private final ContentObserver CONTENT_OBSERVER = new ContentObserver(new Handler()){
        @Override
        public void onChange(boolean selfChange) {
            getLoaderManager().restartLoader(USER_SKILLS_LOADER,null,FragmentSkills.this);
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        getActivity().getContentResolver().registerContentObserver(PortfolioContract.UserSkillsEntry.CONTENT_URI,true, CONTENT_OBSERVER);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUserId = arguments.getLong(MainActivity.USER_ID_PARAMETER);
            mUri = PortfolioContract.UserSkillsEntry.buildUserSkillUri(mUserId);
        }

        View rootView = inflater.inflate(R.layout.fragment_skills, container, false);
        setGraphics(rootView);

        mActivityAdapter = new SkillAdapter(getContext());
        mListView.setAdapter(mActivityAdapter);

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
        getLoaderManager().initLoader(USER_SKILLS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(mUri != null){
            return new CursorLoader(
                    getContext(),
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
        mActivityAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mActivityAdapter.swapCursor(null);
    }
    public void setGraphics(View rootView){
        mListView = (RecyclerView) rootView.findViewById(R.id.activity_listview);
        mListView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    }

}