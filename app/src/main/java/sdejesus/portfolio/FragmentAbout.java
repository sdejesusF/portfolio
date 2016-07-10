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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sdejesus.portfolio.data.PortfolioContract;

/**
 * Created by sdejesus on 6/30/16.
 */

public class FragmentAbout extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mUri;
    private long mUserId;
    private TextView mDescripView;
    private TextView mSkypeView;
    private TextView mPhoneView;
    private TextView mEmailView;
    private TextView mLinkedinView;

    private static final int USER_LOADER = 0;

    private static final String[] DETAIL_COLUMNS = {
        PortfolioContract.UserEntry.COLUMN_SKYPE,
        PortfolioContract.UserEntry.COLUMN_DESCRIPTION,
        PortfolioContract.UserEntry.COLUMN_EMAIL,
        PortfolioContract.UserEntry.COLUMN_NUMBER,
        PortfolioContract.UserEntry.COLUMN_LINKEDIN


    };

    public static final int COL_USER_SKYPE = 0;
    public static final int COL_USER_DESCRIPTION = 1;
    public static final int COL_USER_EMAIL = 2;
    public static final int COL_USER_NUMBER = 3;
    public static final int COL_USER_LINKEDIN = 4;
    public FragmentAbout() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().getContentResolver().registerContentObserver(PortfolioContract.UserEntry.CONTENT_URI,true, CONTENT_OBSERVER);

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
            mUri = PortfolioContract.UserEntry.buildUserUri(mUserId);
        }
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        setGraphics(rootView);
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(USER_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Here we only load user data so there is not needed to switch.
        if(mUri != null){
            return new CursorLoader(
                    getActivity(),
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
            // Read weather condition ID from cursor
            //String weatherId = data.getInt(CO);

            mSkypeView.setText(data.getString(COL_USER_SKYPE));
            mDescripView.setText(data.getString(COL_USER_DESCRIPTION));
            mEmailView.setText(data.getString(COL_USER_EMAIL));
            mPhoneView.setText(data.getString(COL_USER_NUMBER));
            mLinkedinView.setText(data.getString(COL_USER_LINKEDIN));
        }
    }
    private final ContentObserver CONTENT_OBSERVER = new ContentObserver(new Handler()){
        @Override
        public void onChange(boolean selfChange) {
            getLoaderManager().restartLoader(USER_LOADER,null,FragmentAbout.this);
        }
    };
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    public void setGraphics(View rootView){
        mDescripView = (TextView) rootView.findViewById(R.id.about_textview_description);
        mEmailView = (TextView) rootView.findViewById(R.id.about_textview_email);
        mPhoneView = (TextView) rootView.findViewById(R.id.about_textview_phone);
        mSkypeView = (TextView) rootView.findViewById(R.id.about_textview_skype);
        mLinkedinView = (TextView) rootView.findViewById(R.id.about_textview_linkedin);

    }

}