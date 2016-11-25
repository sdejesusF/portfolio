package sdejesus.portfolio;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

import sdejesus.portfolio.custom.CircleImageView;
import sdejesus.portfolio.data.PortfolioContract;
import sdejesus.portfolio.sync.PortfolioSyncAdapter;

public class MainActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    static final String USER_ID_PARAMETER = "USERID";

    private static final int USER_LOADER = 0;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private CircleImageView mAvatarImage;
    private TextView mNameView;

    private Uri mUri;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private static final String[] DETAIL_COLUMNS = {
            PortfolioContract.UserEntry.COLUMN_NAME,
            PortfolioContract.UserEntry.COLUMN_AVATAR
    };

    public static final int COL_USER_NAME = 0;
    public static final int COL_USER_AVATAR = 1;

    private final ContentObserver CONTENT_OBSERVER = new ContentObserver(new Handler()){
        @Override
        public void onChange(boolean selfChange) {
            getSupportLoaderManager().restartLoader(USER_LOADER,null,MainActivity.this);
        }
    };

    // always get the first user because we are not selecting which user portfolio's we want.
    private long mUserId = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Answers(), new Crashlytics());
        setContentView(R.layout.activity_main);

        // bind views
        setGraphics();
        // initializing viewpager
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        // Listener for the bar layout to set title visible when is needed
        mAppBarLayout.addOnOffsetChangedListener(this);
        // set invisible toolbar title
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        mTitle.setText(getString(R.string.about));

        PortfolioSyncAdapter.initializeSyncAdapter(this);
        mUri = PortfolioContract.UserEntry.buildUserUri(mUserId);


        getSupportLoaderManager().initLoader(USER_LOADER, null, this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getContentResolver().registerContentObserver(PortfolioContract.UserEntry.CONTENT_URI,true, CONTENT_OBSERVER);

    }

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(CONTENT_OBSERVER);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;
        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Here we only load user data so there is not needed to switch.
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
            String userName = data.getString(COL_USER_NAME);
            String userAvatar = data.getString(COL_USER_AVATAR);
            int drawableResourceId = this.getResources().getIdentifier(userAvatar, "drawable", this.getPackageName());
            mAvatarImage.setImageResource(drawableResourceId);
            mNameView.setText(userName);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void setGraphics(){
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mAvatarImage = (CircleImageView) findViewById(R.id.main_image);
        mNameView = (TextView) findViewById(R.id.main_textview_name);
    }
    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }
        }else {
            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }
    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
    private void setupViewPager(final ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle arguments = new Bundle();
        arguments.putLong(USER_ID_PARAMETER,mUserId);
        FragmentAbout fragmentAbout = new FragmentAbout();
        fragmentAbout.setArguments(arguments);
        FragmentCategories fragmentCategories = new FragmentCategories();
        fragmentCategories.setArguments(arguments);
        FragmentSkills fragmentSkills = new FragmentSkills();
        fragmentSkills.setArguments(arguments);

        adapter.addFragment(fragmentAbout, getString(R.string.about));
        adapter.addFragment(fragmentCategories, getString(R.string.portfolio));
        adapter.addFragment(fragmentSkills, getString(R.string.interest));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTitle.setText(adapter.mFragmentTitleList.get(position));
                Answers.getInstance().logCustom(new CustomEvent("Tab Pressed " + adapter.mFragmentTitleList.get(position)));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
