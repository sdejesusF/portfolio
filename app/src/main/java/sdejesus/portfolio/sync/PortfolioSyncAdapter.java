package sdejesus.portfolio.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sdejesus.portfolio.R;
import sdejesus.portfolio.Utility;
import sdejesus.portfolio.data.PortfolioContract;

/**
 * Created by sdejesus on 7/3/16.
 */

public class PortfolioSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = PortfolioSyncAdapter.class.getSimpleName();

    public static final int SYNC_INTERVAL = 60;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    public PortfolioSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");
        try {
            JSONObject dataOBJECT = new JSONObject(Utility.getLocalJSON(getContext()));
            final String USERS = "users";
            final String USERS_SKILLS = "users_skills";
            final String ID = "id";
            final String USER_ID = "user_id";
            final String SKYPE = "skype";
            final String NUMBER = "number";
            final String LINKEDIN = "linkedin";
            final String BIRTHDAY = "birthday";
            final String NAME = "name";
            final String EMAIL = "email";
            final String DESCRIPTION = "description";
            final String CATEGORIES = "categories";
            final String CATEGORY_ID = "category_id";
            final String AVATAR = "avatar";
            final String PROJECTS = "projects";
            final String REPOSITORY = "repository";
            final String LINK = "link";
            final String PROJECT_GALLERY = "project_gallery";
            final String PROJECT_ID = "project_id";
            final String YEAR = "year";
            final String TAGS = "tags";
            final String COMPANY = "company";

            // sync users data
            JSONArray usersArray = dataOBJECT.getJSONArray(USERS);
            int usersLen = usersArray.length();
            for(int i = 0; i < usersLen; i++){
                JSONObject userObject = usersArray.getJSONObject(i);
                ContentValues userValues = new ContentValues();
                userValues.put(PortfolioContract.UserEntry._ID,userObject.getLong(ID));
                userValues.put(PortfolioContract.UserEntry.COLUMN_NAME,userObject.getString(NAME));
                userValues.put(PortfolioContract.UserEntry.COLUMN_EMAIL,userObject.getString(EMAIL));
                userValues.put(PortfolioContract.UserEntry.COLUMN_DESCRIPTION,userObject.getString(DESCRIPTION));
                userValues.put(PortfolioContract.UserEntry.COLUMN_NUMBER,userObject.getString(NUMBER));
                userValues.put(PortfolioContract.UserEntry.COLUMN_SKYPE,userObject.getString(SKYPE));
                userValues.put(PortfolioContract.UserEntry.COLUMN_AVATAR,userObject.getString(AVATAR));
                userValues.put(PortfolioContract.UserEntry.COLUMN_LINKEDIN,userObject.getString(LINKEDIN));
                addOrUpdate(userValues, PortfolioContract.UserEntry.CONTENT_URI, PortfolioContract.UserEntry._ID);
            }

            // sync user skills data
            JSONArray usersSkillsArray = dataOBJECT.getJSONArray(USERS_SKILLS);
            int usersSkillsLen = usersSkillsArray.length();
            for(int i = 0; i < usersSkillsLen; i++){
                JSONObject usersSkillObject = usersSkillsArray.getJSONObject(i);
                ContentValues usersSkillValues = new ContentValues();
                usersSkillValues.put(PortfolioContract.UserSkillsEntry._ID,usersSkillObject.getLong(ID));
                usersSkillValues.put(PortfolioContract.UserSkillsEntry.COLUMN_NAME,usersSkillObject.getString(NAME));
                usersSkillValues.put(PortfolioContract.UserSkillsEntry.COLUMN_DESCRIPTION,usersSkillObject.getString(DESCRIPTION));
                usersSkillValues.put(PortfolioContract.UserSkillsEntry.COLUMN_AVATAR,usersSkillObject.getString(AVATAR));
                usersSkillValues.put(PortfolioContract.UserSkillsEntry.COLUMN_USER_KEY,usersSkillObject.getLong(USER_ID));

                addOrUpdate(usersSkillValues, PortfolioContract.UserSkillsEntry.CONTENT_URI, PortfolioContract.UserSkillsEntry._ID);
            }

            // syncs Categories data
            JSONArray categoriesArray = dataOBJECT.getJSONArray(CATEGORIES);
            int categoriesLen = categoriesArray.length();
            for(int i = 0; i < categoriesLen; i++){
                JSONObject categoryObject = categoriesArray.getJSONObject(i);
                ContentValues categoryValues = new ContentValues();
                categoryValues.put(PortfolioContract.CategoryEntry._ID,categoryObject.getLong(ID));
                categoryValues.put(PortfolioContract.CategoryEntry.COLUMN_NAME,categoryObject.getString(NAME));
                categoryValues.put(PortfolioContract.CategoryEntry.COLUMN_DESCRIPTION,categoryObject.getString(DESCRIPTION));
                categoryValues.put(PortfolioContract.CategoryEntry.COLUMN_AVATAR,categoryObject.getString(AVATAR));
                addOrUpdate(categoryValues, PortfolioContract.CategoryEntry.CONTENT_URI, PortfolioContract.CategoryEntry._ID);
            }

            // syncs Categories data
            JSONArray projectsArray = dataOBJECT.getJSONArray(PROJECTS);
            int projectsLen = projectsArray.length();
            for(int i = 0; i < projectsLen; i++){
                JSONObject projectObject = projectsArray.getJSONObject(i);
                ContentValues projectValues = new ContentValues();
                projectValues.put(PortfolioContract.ProjectEntry._ID,projectObject.getLong(ID));
                projectValues.put(PortfolioContract.ProjectEntry.COLUMN_NAME,projectObject.getString(NAME));
                projectValues.put(PortfolioContract.ProjectEntry.COLUMN_DESCRIPTION,projectObject.getString(DESCRIPTION));
                projectValues.put(PortfolioContract.ProjectEntry.COLUMN_AVATAR,projectObject.getString(AVATAR));
                projectValues.put(PortfolioContract.ProjectEntry.COLUMN_LINK,projectObject.getString(LINK));
                projectValues.put(PortfolioContract.ProjectEntry.COLUMN_REPOSITORY,projectObject.getString(REPOSITORY));
                projectValues.put(PortfolioContract.ProjectEntry.COLUMN_TAGS,projectObject.getString(TAGS));
                projectValues.put(PortfolioContract.ProjectEntry.COLUMN_YEAR,projectObject.getString(YEAR));
                projectValues.put(PortfolioContract.ProjectEntry.COLUMN_COMPANY,projectObject.getString(COMPANY));
                projectValues.put(PortfolioContract.ProjectEntry.COLUMN_USER_KEY,projectObject.getLong(USER_ID));
                projectValues.put(PortfolioContract.ProjectEntry.COLUMN_CATEGORY_KEY,projectObject.getLong(CATEGORY_ID));

                addOrUpdate(projectValues, PortfolioContract.ProjectEntry.CONTENT_URI, PortfolioContract.ProjectEntry._ID);
            }

            // sync project gallery data
            JSONArray projectGalleryArray = dataOBJECT.getJSONArray(PROJECT_GALLERY);
            int projectGalleryArrayLen = projectGalleryArray.length();
            for(int i = 0; i < projectGalleryArrayLen; i++){
                JSONObject projectGalleryObject = projectGalleryArray.getJSONObject(i);
                ContentValues projectGalleryValues = new ContentValues();
                projectGalleryValues.put(PortfolioContract.ProjectGalleryEntry._ID,projectGalleryObject.getLong(ID));
                projectGalleryValues.put(PortfolioContract.ProjectGalleryEntry.COLUMN_DESCRIPTION,projectGalleryObject.getString(DESCRIPTION));
                projectGalleryValues.put(PortfolioContract.ProjectGalleryEntry.COLUMN_AVATAR,projectGalleryObject.getString(AVATAR));
                projectGalleryValues.put(PortfolioContract.ProjectGalleryEntry.COLUMN_PROJECT_KEY,projectGalleryObject.getLong(PROJECT_ID));
                addOrUpdate(projectGalleryValues, PortfolioContract.ProjectGalleryEntry.CONTENT_URI, PortfolioContract.ProjectGalleryEntry._ID);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }
    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }
    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }
    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        PortfolioSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
    long addOrUpdate(ContentValues values, Uri content_uri,String id){
        long resultId;

        // First, check if the location with this city name exists in the db
        Cursor cursor = getContext().getContentResolver().query(
                content_uri,
                new String[]{id},
                id + " = ?",
                new String[]{String.valueOf(values.getAsLong(id))},
                null);
        if (cursor.moveToFirst()) {
            resultId = values.getAsLong(id);
            // Finally, insert location data into the database.
            getContext().getContentResolver().update(
                    content_uri,
                    values,
                    id + "=?",
                    new String[]{String.valueOf(resultId)}
            );
        } else {

            // Finally, insert location data into the database.
            Uri insertedUri = getContext().getContentResolver().insert(
                    content_uri,
                    values
            );

            // The resulting URI contains the ID for the row.  Extract the userId from the Uri.
            resultId = ContentUris.parseId(insertedUri);
        }

        cursor.close();
        return resultId;
    }
}
