package sdejesus.portfolio.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import sdejesus.portfolio.data.PortfolioContract.CategoryEntry;
import sdejesus.portfolio.data.PortfolioContract.ProjectEntry;
import sdejesus.portfolio.data.PortfolioContract.ProjectGalleryEntry;
import sdejesus.portfolio.data.PortfolioContract.UserEntry;
import sdejesus.portfolio.data.PortfolioContract.UserSkillsEntry;



/**
 * Created by sdejesus on 6/28/16.
 */

public class testContentProvider extends AndroidTestCase {

    public static final String LOG_TAG = testContentProvider.class.getSimpleName();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecordsFromProvider();
    }
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                UserEntry.CONTENT_URI,
                null,
                null
        );
        Cursor cursor = mContext.getContentResolver().query(
                CategoryEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from User table during delete", 0, cursor.getCount());
        cursor.close();

        mContext.getContentResolver().delete(
                UserSkillsEntry.CONTENT_URI,
                null,
                null
        );
        cursor = mContext.getContentResolver().query(
                UserSkillsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from UserActivities table during delete", 0, cursor.getCount());
        cursor.close();

        mContext.getContentResolver().delete(
                CategoryEntry.CONTENT_URI,
                null,
                null
        );
        cursor = mContext.getContentResolver().query(
                CategoryEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Category table during delete", 0, cursor.getCount());
        cursor.close();

        mContext.getContentResolver().delete(
                ProjectEntry.CONTENT_URI,
                null,
                null
        );
        cursor = mContext.getContentResolver().query(
                ProjectEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Project table during delete", 0, cursor.getCount());
        cursor.close();

        mContext.getContentResolver().delete(
                ProjectGalleryEntry.CONTENT_URI,
                null,
                null
        );
        cursor = mContext.getContentResolver().query(
                ProjectGalleryEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from ProjectGallery table during delete", 0, cursor.getCount());
        cursor.close();
    }
    public void testInsertReadProvider() {
        ContentValues testValues = TestUtilities.createUserValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(UserEntry.CONTENT_URI, true, tco);
        Uri locationUri = mContext.getContentResolver().insert(UserEntry.CONTENT_URI, testValues);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long userRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back.
        assertTrue(userRowId != -1);


        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                UserEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating UserEntry.",
                cursor, testValues);


    }
}
