package sdejesus.portfolio.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

import sdejesus.portfolio.utils.PollingCheck;

/**
 * Created by sdejesus on 6/17/16.
 */
public class TestUtilities extends AndroidTestCase {

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }
    static ContentValues createUserValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(PortfolioContract.UserEntry.COLUMN_AVATAR, "");
        testValues.put(PortfolioContract.UserEntry.COLUMN_NAME, "Sergio De Jesus");
        testValues.put(PortfolioContract.UserEntry.COLUMN_DESCRIPTION, "Hi, im Sergio and im testing with unit test");
        testValues.put(PortfolioContract.UserEntry.COLUMN_EMAIL, "email@email.com");
        testValues.put(PortfolioContract.UserEntry.COLUMN_NUMBER, "+584123537041");
        testValues.put(PortfolioContract.UserEntry.COLUMN_SKYPE, "scs.sdejesus");
        testValues.put(PortfolioContract.UserEntry.COLUMN_LINKEDIN, "http://linkedin.com/asdf");

        testValues.put(PortfolioContract.UserEntry.COLUMN_BIRTHDAY, 100);


        return testValues;
    }
    static ContentValues createCategoryValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(PortfolioContract.CategoryEntry.COLUMN_AVATAR, "");
        testValues.put(PortfolioContract.CategoryEntry.COLUMN_NAME, "Android");
        testValues.put(PortfolioContract.CategoryEntry.COLUMN_DESCRIPTION, "Applications made for Android devices");

        return testValues;
    }

    static long insertUserValues(Context context) {
        // insert our test records into the database
        PortfolioDbHelper dbHelper = new PortfolioDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createUserValues();

        long userRowId;
        userRowId = db.insert(PortfolioContract.UserEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert North Pole Location Values", userRowId != -1);

        return userRowId;
    }
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
