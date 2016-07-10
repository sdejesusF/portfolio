package sdejesus.portfolio.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;


/**
 * Created by sdejesus on 6/17/16.
 */
public class PortfolioProvider extends ContentProvider {

    public final String LOG_TAG = PortfolioProvider.class.getSimpleName();
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private PortfolioDbHelper mOpenHelper;

    static final int USER = 100;
    static final int USER_ID = 101;

    static final int USER_SKILLS = 200;
    static final int USER_SKILLS_ID = 201;

    static final int CATEGORY = 300;

    static final int PROJECT = 400;
    static final int PROJECT_ID = 401;
    static final int PROJECT_CATEGORY_AND_USER = 403;

    static final int PROJECT_GALLERY = 500;
    static final int PROJECT_GALLERY_ID = 501;

    private static final SQLiteQueryBuilder sProjectQueryBuilder;
    private static final SQLiteQueryBuilder sProjectGalleyQueryBuilder;

    private static final SQLiteQueryBuilder sUserActivityQueryBuilder;

    static{
        sProjectGalleyQueryBuilder = new SQLiteQueryBuilder();
        sProjectGalleyQueryBuilder.setTables(PortfolioContract.ProjectGalleryEntry.TABLE_NAME);
    }
    static{
        sProjectQueryBuilder = new SQLiteQueryBuilder();
        sProjectQueryBuilder.setTables(PortfolioContract.ProjectEntry.TABLE_NAME);
    }
    static{
        sUserActivityQueryBuilder = new SQLiteQueryBuilder();
        sUserActivityQueryBuilder.setTables(PortfolioContract.UserSkillsEntry.TABLE_NAME);
    }
    private static final  String sProjectGallerySelection =
            PortfolioContract.ProjectGalleryEntry.COLUMN_PROJECT_KEY + " = ? ";
    // user_id = ?
    private static final String sUserActivitySelection =
            PortfolioContract.UserSkillsEntry.COLUMN_USER_KEY + " = ? ";
    //category_id = ? AND user_id = ?
    private static final String sCategoryAndUserSelection =
            PortfolioContract.ProjectEntry.COLUMN_CATEGORY_KEY + " = ? AND " +
                    PortfolioContract.ProjectEntry.COLUMN_USER_KEY + " = ? ";
    //_id = ?
    private static final String sPortfolioIdSelection =
            PortfolioContract.ProjectEntry._ID + " = ?";
    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PortfolioContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, PortfolioContract.PATH_USER, USER);
        matcher.addURI(authority, PortfolioContract.PATH_USER + "/#", USER_ID);

        matcher.addURI(authority, PortfolioContract.PATH_USER_SKILL, USER_SKILLS);
        matcher.addURI(authority, PortfolioContract.PATH_USER_SKILL + "/#", USER_SKILLS_ID);

        matcher.addURI(authority, PortfolioContract.PATH_CATEGORY, CATEGORY);

        matcher.addURI(authority, PortfolioContract.PATH_PROJECT, PROJECT);
        matcher.addURI(authority, PortfolioContract.PATH_PROJECT + "/#", PROJECT_ID);
        //get projects for user and categories
        matcher.addURI(authority, PortfolioContract.PATH_PROJECT + "/#/#", PROJECT_CATEGORY_AND_USER);

        matcher.addURI(authority, PortfolioContract.PATH_PROJECT_GALLERY, PROJECT_GALLERY);
        matcher.addURI(authority, PortfolioContract.PATH_PROJECT_GALLERY + "/#", PROJECT_GALLERY_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new PortfolioDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case USER:
            case USER_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                    PortfolioContract.UserEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                );
            break;
            case USER_SKILLS:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        PortfolioContract.UserSkillsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case USER_SKILLS_ID:
                retCursor = getActivitiesForUser(uri,projection,sortOrder);
                break;

            case CATEGORY:
                retCursor = mOpenHelper.getReadableDatabase().query(
                    PortfolioContract.CategoryEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                );
                break;
            case PROJECT:
                retCursor = mOpenHelper.getReadableDatabase().query(
                    PortfolioContract.ProjectEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                );
                break;
            case PROJECT_ID:
                retCursor = getProjectForID(uri,projection,sortOrder);
                break;
            case PROJECT_CATEGORY_AND_USER:
                retCursor = getProjectByCategoryAndUser(uri,projection,sortOrder);
                break;
            case PROJECT_GALLERY:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        PortfolioContract.ProjectGalleryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PROJECT_GALLERY_ID:
                retCursor = getProjectGalleryByProject(uri,projection,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {

            case USER:
                return PortfolioContract.UserEntry.CONTENT_TYPE;
            case USER_ID:
                return PortfolioContract.UserEntry.CONTENT_ITEM_TYPE;
            case USER_SKILLS:
                return PortfolioContract.UserSkillsEntry.CONTENT_TYPE;
            case USER_SKILLS_ID:
                return PortfolioContract.UserSkillsEntry.CONTENT_TYPE;
            case CATEGORY:
                return PortfolioContract.CategoryEntry.CONTENT_TYPE;
            case PROJECT:
                return PortfolioContract.ProjectEntry.CONTENT_TYPE;
            case PROJECT_ID:
                return PortfolioContract.ProjectEntry.CONTENT_ITEM_TYPE;
            case PROJECT_CATEGORY_AND_USER:
                return PortfolioContract.ProjectEntry.CONTENT_TYPE;
            case PROJECT_GALLERY:
                return PortfolioContract.ProjectGalleryEntry.CONTENT_TYPE;
            case PROJECT_GALLERY_ID:
                return PortfolioContract.ProjectGalleryEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        long id;
        String errorMessage = "Failed to insert row into " + uri;
        switch (match){

            case USER:
                id = db.insert(PortfolioContract.UserEntry.TABLE_NAME, null, values);
                if ( id > 0 )
                    returnUri = PortfolioContract.UserEntry.buildUserUri(id);
                else
                    throw new android.database.SQLException(errorMessage);
                break;
            case USER_SKILLS:
                id = db.insert(PortfolioContract.UserSkillsEntry.TABLE_NAME, null, values);
                if ( id > 0 )
                    returnUri = PortfolioContract.UserSkillsEntry.buildUserSkillUri(id);
                else
                    throw new android.database.SQLException(errorMessage);
                break;
            case CATEGORY:
                id = db.insert(PortfolioContract.CategoryEntry.TABLE_NAME, null, values);
                if ( id > 0 )
                    returnUri = PortfolioContract.CategoryEntry.buildCategoryUri(id);
                else
                    throw new android.database.SQLException(errorMessage);
                break;
            case PROJECT:
                id = db.insert(PortfolioContract.ProjectEntry.TABLE_NAME, null, values);
                if ( id > 0 )
                    returnUri = PortfolioContract.ProjectEntry.buildProjectUri(id);
                else
                    throw new android.database.SQLException(errorMessage);
                break;
            case PROJECT_GALLERY:
                id = db.insert(PortfolioContract.ProjectGalleryEntry.TABLE_NAME, null, values);
                if ( id > 0 )
                    returnUri = PortfolioContract.ProjectGalleryEntry.buildProjectGalleryUri(id);
                else
                    throw new android.database.SQLException(errorMessage);
                break;
            default:
                throw  new android.database.SQLException("Unknown uri " + uri);

        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match){

            case USER:
                rowsDeleted = db.delete(PortfolioContract.UserEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case USER_SKILLS:
                rowsDeleted = db.delete(PortfolioContract.UserSkillsEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case CATEGORY:
                rowsDeleted = db.delete(PortfolioContract.CategoryEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case PROJECT:
                rowsDeleted = db.delete(PortfolioContract.ProjectEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case PROJECT_GALLERY:
                rowsDeleted = db.delete(PortfolioContract.ProjectGalleryEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            default:
                throw  new android.database.SQLException("Unknown uri " + uri);

        }
        if(rowsDeleted > 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match){

            case USER:
                rowsUpdated = db.update(PortfolioContract.UserEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case USER_SKILLS:
                rowsUpdated = db.update(PortfolioContract.UserSkillsEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case CATEGORY:
                rowsUpdated = db.update(PortfolioContract.CategoryEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case PROJECT:
                rowsUpdated = db.update(PortfolioContract.ProjectEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case PROJECT_GALLERY:
                rowsUpdated = db.update(PortfolioContract.ProjectGalleryEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw  new android.database.SQLException("Unknown uri " + uri);

        }
        if(rowsUpdated > 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        String tableName;
        switch (match) {
            case USER_SKILLS:
                tableName = PortfolioContract.UserSkillsEntry.TABLE_NAME;
                break;
            case PROJECT:
                tableName = PortfolioContract.UserSkillsEntry.TABLE_NAME;
                break;
            case PROJECT_GALLERY:
                tableName = PortfolioContract.ProjectGalleryEntry.TABLE_NAME;
                break;
            default:
                return super.bulkInsert(uri, values);
        }
        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {
                long _id = db.insert(tableName, null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;

    }

    @Nullable
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
    private Cursor getProjectGalleryByProject(
            Uri uri, String[] projection, String sortOrder) {
        String projectID = PortfolioContract.ProjectGalleryEntry.getProjectFromUri(uri);
        return sProjectGalleyQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sProjectGallerySelection,
                new String[]{projectID},
                null,
                null,
                sortOrder
        );
    }
    private Cursor getProjectByCategoryAndUser(
            Uri uri, String[] projection, String sortOrder) {
        String categoryId = PortfolioContract.ProjectEntry.getCategoryFromUri(uri);
        String userId = PortfolioContract.ProjectEntry.getUserFromUri(uri);

        return sProjectQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sCategoryAndUserSelection,
                new String[]{categoryId, userId},
                null,
                null,
                sortOrder
        );
    }
    private Cursor getActivitiesForUser(
            Uri uri, String[] projection, String sortOrder) {
        String userId = PortfolioContract.UserSkillsEntry.getUserFromUri(uri);
        return sUserActivityQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sUserActivitySelection,
                new String[]{userId},
                null,
                null,
                sortOrder
        );
    }
    private Cursor getProjectForID(
            Uri uri, String[] projection, String sortOrder) {
        String projectID = PortfolioContract.ProjectEntry.getProjectFromUri(uri);
        return sProjectQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sPortfolioIdSelection,
                new String[]{projectID},
                null,
                null,
                sortOrder
        );
    }
}
