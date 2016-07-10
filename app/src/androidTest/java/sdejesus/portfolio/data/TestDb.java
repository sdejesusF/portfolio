package sdejesus.portfolio.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by sdejesus on 6/17/16.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(PortfolioDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(PortfolioContract.UserEntry.TABLE_NAME);

        mContext.deleteDatabase(PortfolioDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new PortfolioDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain the user table
        assertTrue("Error: Your database was created without the user table",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + PortfolioContract.UserEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> userColumnHashSet = new HashSet<String>();
        userColumnHashSet.add(PortfolioContract.UserEntry._ID);
        userColumnHashSet.add(PortfolioContract.UserEntry.COLUMN_AVATAR);
        userColumnHashSet.add(PortfolioContract.UserEntry.COLUMN_BIRTHDAY);
        userColumnHashSet.add(PortfolioContract.UserEntry.COLUMN_DESCRIPTION);
        userColumnHashSet.add(PortfolioContract.UserEntry.COLUMN_EMAIL);
        userColumnHashSet.add(PortfolioContract.UserEntry.COLUMN_NAME);
        userColumnHashSet.add(PortfolioContract.UserEntry.COLUMN_NUMBER);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            userColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required user
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required user entry columns",
                userColumnHashSet.isEmpty());
        c.close();
        db.close();
    }

    public void testUserTable() {
        insertUser();
    }
    public void testCategoryTable() throws Throwable {
        insertCategory();
    }
    public void testProjectTable(){

        long projectRowId = insertProject();


        PortfolioDbHelper dbHelper = new PortfolioDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        assertFalse("Error: Project Not Inserted Correctly",projectRowId == -1F);

        Cursor projectCursor = db.query(
                PortfolioContract.ProjectEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue( "Error: No Records returned from project query", projectCursor.moveToFirst() );

        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();

        // Fifth Step: Validate the project Query
        TestUtilities.validateCurrentRecord("testInsertReadDb projectEntry failed to validate",
                projectCursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from project query",
                projectCursor.moveToNext() );

        // Sixth Step: Close cursor and database
        projectCursor.close();
        dbHelper.close();

    }
    public void testUserActivity(){

        long userRowId = insertUser();
        // Make sure we have a valid row ID.
        assertFalse("Error: User Not Inserted Correctly", userRowId == -1L);

        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(PortfolioContract.UserSkillsEntry.COLUMN_AVATAR, "");
        testValues.put(PortfolioContract.UserSkillsEntry.COLUMN_NAME, "Surf");
        testValues.put(PortfolioContract.UserSkillsEntry.COLUMN_DESCRIPTION, "sdafs asdfs asdfa dfasd fas asdf asdfa dfa dsfasd fasdfa sdf");
        testValues.put(PortfolioContract.UserSkillsEntry.COLUMN_USER_KEY, userRowId);

        PortfolioDbHelper dbHelper = new PortfolioDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long userActivityRowId = db.insert(PortfolioContract.UserSkillsEntry.TABLE_NAME,null,testValues);

        assertFalse("Error: UserActivites Not Inserted Correctly",userActivityRowId == -1F);

        Cursor userActivityCursor = db.query(
                PortfolioContract.UserSkillsEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue( "Error: No Records returned from user activities query", userActivityCursor.moveToFirst() );

        // Validate the project Query
        TestUtilities.validateCurrentRecord("testInsertReadDb userActivityEntry failed to validate",
                userActivityCursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from user activity query",
                userActivityCursor.moveToNext() );

        // Close cursor and database
        userActivityCursor.close();
        dbHelper.close();

    }
    public void testProjectGallery(){

        long projectRowId = insertProject();

        // Make sure we have a valid row ID.
        assertFalse("Error: Project Not Inserted Correctly", projectRowId == -1L);

        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(PortfolioContract.ProjectGalleryEntry.COLUMN_AVATAR, "");
        testValues.put(PortfolioContract.ProjectGalleryEntry.COLUMN_DESCRIPTION, "sdafs asdfs asdfa dfasd fas asdf asdfa dfa dsfasd fasdfa sdf");
        testValues.put(PortfolioContract.ProjectGalleryEntry.COLUMN_PROJECT_KEY, projectRowId);

        ContentValues testValues2 = new ContentValues();
        testValues2.put(PortfolioContract.ProjectGalleryEntry.COLUMN_AVATAR, "");
        testValues2.put(PortfolioContract.ProjectGalleryEntry.COLUMN_DESCRIPTION, "sdafs asdfs asdfa dfasd fas asdf asdfa dfa dsfasd fasdfa sdf");
        testValues2.put(PortfolioContract.ProjectGalleryEntry.COLUMN_PROJECT_KEY, projectRowId);

        ContentValues testValues3 = new ContentValues();
        testValues3.put(PortfolioContract.ProjectGalleryEntry.COLUMN_AVATAR, "");
        testValues3.put(PortfolioContract.ProjectGalleryEntry.COLUMN_DESCRIPTION, "sdafs asdfs asdfa dfasd fas asdf asdfa dfa dsfasd fasdfa sdf");
        testValues3.put(PortfolioContract.ProjectGalleryEntry.COLUMN_PROJECT_KEY, projectRowId);

        PortfolioDbHelper dbHelper = new PortfolioDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long projectGalleryRowId = db.insert(PortfolioContract.ProjectGalleryEntry.TABLE_NAME,null,testValues);
        long projectGalleryRowId2 = db.insert(PortfolioContract.ProjectGalleryEntry.TABLE_NAME,null,testValues2);
        long projectGalleryRowId3 = db.insert(PortfolioContract.ProjectGalleryEntry.TABLE_NAME,null,testValues3);

        assertFalse("Error: ProjectGallery Not Inserted Correctly",projectGalleryRowId == -1F);
        assertFalse("Error: ProjectGallery2 Not Inserted Correctly",projectGalleryRowId2 == -1F);
        assertFalse("Error: ProjectGallery3 Not Inserted Correctly",projectGalleryRowId3 == -1F);

        db.close();
    }
    public long insertUser() {
        // Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        PortfolioDbHelper dbHelper = new PortfolioDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create ContentValues of what to insert
        ContentValues testValues = TestUtilities.createUserValues();

        // Insert ContentValues into database and get a row ID back
        long userRowId;
        userRowId = db.insert(PortfolioContract.UserEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("We do not get any row", userRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                PortfolioContract.UserEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue( "Error: No Records returned from user query", cursor.moveToFirst() );

        TestUtilities.validateCurrentRecord("Error: User Query Validation Failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from user query",
                cursor.moveToNext() );

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
        return userRowId;
    }
    public long insertCategory(){

        PortfolioDbHelper dbHelper = new PortfolioDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create ContentValues of what to insert
        ContentValues testValues = TestUtilities.createCategoryValues();

        // Insert ContentValues into database and get a row ID back
        long categoryRowId;
        categoryRowId = db.insert(PortfolioContract.CategoryEntry.TABLE_NAME, null, testValues);

        assertTrue("We do not get any row back " + categoryRowId, categoryRowId != -1);

        db.close();
        return categoryRowId;
    }
    public long insertProject(){
        long userRowId = insertUser();

        // Make sure we have a valid row ID.
        assertFalse("Error: User Not Inserted Correctly", userRowId == -1L);

        long categoryRowId = insertCategory();
        // Make sure we have a valid row ID.
        assertFalse("Error: Category Not Inserted Correctly", categoryRowId == -1L);

        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(PortfolioContract.ProjectEntry.COLUMN_AVATAR, "");
        testValues.put(PortfolioContract.ProjectEntry.COLUMN_NAME, "Pebble Plus");
        testValues.put(PortfolioContract.ProjectEntry.COLUMN_DESCRIPTION, "sdafs asdfs asdfa dfasd fas asdf asdfa dfa dsfasd fasdfa sdf");
        testValues.put(PortfolioContract.ProjectEntry.COLUMN_LINK, "http://");
        testValues.put(PortfolioContract.ProjectEntry.COLUMN_REPOSITORY, "http://");
        testValues.put(PortfolioContract.ProjectEntry.COLUMN_USER_KEY, userRowId);
        testValues.put(PortfolioContract.ProjectEntry.COLUMN_CATEGORY_KEY, categoryRowId);

        PortfolioDbHelper dbHelper = new PortfolioDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long projectRowId = db.insert(PortfolioContract.ProjectEntry.TABLE_NAME,null,testValues);

        assertFalse("Error: Project Not Inserted Correctly",projectRowId == -1F);

        return projectRowId;
    }
}
