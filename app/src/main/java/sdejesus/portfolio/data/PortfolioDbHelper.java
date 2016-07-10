package sdejesus.portfolio.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sdejesus.portfolio.data.PortfolioContract.CategoryEntry;
import sdejesus.portfolio.data.PortfolioContract.ProjectEntry;
import sdejesus.portfolio.data.PortfolioContract.ProjectGalleryEntry;
import sdejesus.portfolio.data.PortfolioContract.UserEntry;
import sdejesus.portfolio.data.PortfolioContract.UserSkillsEntry;

/**
 * Created by sdejesus on 6/17/16.
 */
public class PortfolioDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 8;

    static final String DATABASE_NAME = "portfolio.db";

    public PortfolioDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold Users.
        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                UserEntry._ID + " INTEGER PRIMARY KEY," +
                UserEntry.COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
                UserEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                UserEntry.COLUMN_NUMBER + " TEXT NOT NULL, " +
                UserEntry.COLUMN_SKYPE + " TEXT NOT NULL, " +
                UserEntry.COLUMN_LINKEDIN + " TEXT NOT NULL, " +
                UserEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                UserEntry.COLUMN_BIRTHDAY + " INTEGER, " +
                UserEntry.COLUMN_AVATAR + " TEXT NOT NULL " +
                " );";

        // Create a table to hold User Skill.
        final String SQL_CREATE_USER_ACTIVITY_TABLE = "CREATE TABLE " + UserSkillsEntry.TABLE_NAME + " (" +
                UserSkillsEntry._ID + " INTEGER PRIMARY KEY," +
                UserSkillsEntry.COLUMN_AVATAR + " TEXT NOT NULL, " +
                UserSkillsEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                UserSkillsEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                UserSkillsEntry.COLUMN_USER_KEY + " INTEGER NOT NULL, " +

                // Set up the user column as a foreign key to User table.
                " FOREIGN KEY (" + UserSkillsEntry.COLUMN_USER_KEY + ") REFERENCES " +
                UserEntry.TABLE_NAME + " (" + UserEntry._ID + ") " +


                " );";

        // Create a table to hold Users.
        final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + CategoryEntry.TABLE_NAME + " (" +
                CategoryEntry._ID + " INTEGER PRIMARY KEY," +
                CategoryEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                CategoryEntry.COLUMN_AVATAR + " TEXT NOT NULL, " +
                CategoryEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL " +
                " );";

        // Create a table to hold Projects.
        final String SQL_CREATE_PROJECT_TABLE = "CREATE TABLE " + ProjectEntry.TABLE_NAME + " (" +
                ProjectEntry._ID + " INTEGER PRIMARY KEY," +
                ProjectEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ProjectEntry.COLUMN_AVATAR + " TEXT NOT NULL, " +
                ProjectEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                ProjectEntry.COLUMN_LINK + " TEXT, " +
                ProjectEntry.COLUMN_REPOSITORY + " TEXT, " +
                ProjectEntry.COLUMN_YEAR + " TEXT, " +
                ProjectEntry.COLUMN_COMPANY + " TEXT, " +
                ProjectEntry.COLUMN_TAGS + " TEXT, " +
                ProjectEntry.COLUMN_CATEGORY_KEY + " INTEGER NOT NULL, " +
                ProjectEntry.COLUMN_USER_KEY + " INTEGER NOT NULL, " +

                // Set up the user column as a foreign key to User table.
                " FOREIGN KEY (" + ProjectEntry.COLUMN_USER_KEY + ") REFERENCES " +
                UserEntry.TABLE_NAME + " (" + UserEntry._ID + "), " +

                // Set up the category column as a foreign key to Category table.
                " FOREIGN KEY (" + ProjectEntry.COLUMN_CATEGORY_KEY + ") REFERENCES " +
                CategoryEntry.TABLE_NAME + " (" + CategoryEntry._ID + ") " +

                " );";

        // Create a table to hold Project gallery.
        final String SQL_CREATE_PROJECT_GALLERY_TABLE = "CREATE TABLE " + ProjectGalleryEntry.TABLE_NAME + " (" +
                ProjectGalleryEntry._ID + " INTEGER PRIMARY KEY," +
                ProjectGalleryEntry.COLUMN_AVATAR + " TEXT NOT NULL, " +
                ProjectGalleryEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                ProjectGalleryEntry.COLUMN_PROJECT_KEY + " INTEGER NOT NULL, " +

                // Set up the user column as a foreign key to User table.
                " FOREIGN KEY (" + ProjectGalleryEntry.COLUMN_PROJECT_KEY + ") REFERENCES " +
                ProjectEntry.TABLE_NAME + " (" + ProjectEntry._ID + ") " +


                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USER_ACTIVITY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PROJECT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PROJECT_GALLERY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Here we usually execute the ALTER, DROP TABLE or any other sentence that would change a database.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserSkillsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProjectEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProjectGalleryEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
