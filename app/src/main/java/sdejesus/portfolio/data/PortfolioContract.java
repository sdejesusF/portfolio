package sdejesus.portfolio.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the portfolio database.
 */
public class PortfolioContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "sdejesus.portfolio";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // content://sdejesus.portfolio.app/user/
    public static final String PATH_USER = "user";
    public static final String PATH_USER_SKILL = "user_activity";
    public static final String PATH_CATEGORY = "category";
    public static final String PATH_PROJECT = "project";
    public static final String PATH_PROJECT_GALLERY = "project_gallery";

    /* Inner class that defines the table contents of the user table */
    public static final class UserEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        // Table name
        public static final String TABLE_NAME = "user";
        // Columns name
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_BIRTHDAY = "birthday";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_SKYPE = "skype";
        public static final String COLUMN_LINKEDIN = "linkedin";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_AVATAR = "avatar";

        public static Uri buildUserUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
    /* Inner class that defines the table contents of the User Skills table */
    public static final class UserSkillsEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER_SKILL).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER_SKILL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER_SKILL;

        // Table name
        public static final String TABLE_NAME = "user_skills";
        // Columns name
        public static final String COLUMN_AVATAR = "avatar";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_USER_KEY = "user_id";

        public static Uri buildUserSkillUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
        public static String getUserFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }
    /* Inner class that defines the table contents of the Category table */
    public static final class CategoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORY).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;

        // Table name
        public static final String TABLE_NAME = "category";
        // Columns name
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_AVATAR = "avatar";

        public static Uri buildCategoryUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

    }

    /* Inner class that defines the table contents of the Project table */
    public static final class ProjectEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROJECT).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROJECT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROJECT;

        // Table name
        public static final String TABLE_NAME = "project";
        // Columns name
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_AVATAR = "avatar";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_REPOSITORY = "repository";
        public static final String COLUMN_COMPANY = "company";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_TAGS = "tags";
        public static final String COLUMN_USER_KEY = "user_id";
        public static final String COLUMN_CATEGORY_KEY = "category_id";

        public static Uri buildProjectUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
        public static Uri buildProjectByCategoryAndUser(long category_id, long user_id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(category_id))
                    .appendPath(Long.toString(user_id)).build();
        }
        public static String getCategoryFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getUserFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static String getProjectFromUri(Uri uri) { return uri.getPathSegments().get(1); }
    }
    /* Inner class that defines the table contents of the project gallery table */
    public static final class ProjectGalleryEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROJECT_GALLERY).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROJECT_GALLERY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROJECT_GALLERY;

        // Table name
        public static final String TABLE_NAME = "project_gallery";
        // Columns name
        public static final String COLUMN_AVATAR = "avatar";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PROJECT_KEY = "project_id";

        public static Uri buildProjectGalleryUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
        public static String getProjectFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }

}
