package portfolio.saurabh.popularmovies.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

public class MyContentProvider extends ContentProvider {
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/favorites";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/favorite";
    static final String PROVIDER_NAME = "portfolio.saurabh.popularmovies.provider";
    static final String URL = "content://" + PROVIDER_NAME + '/' + MyDatabaseHelper.TABLE_FAVORITES;
    static final Uri CONTENT_URI = Uri.parse(URL);
    private static final int FAVORITES = 1;
    private static final int FAVORITE_ID = 2;
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(PROVIDER_NAME, MyDatabaseHelper.TABLE_FAVORITES, FAVORITES);
        sURIMatcher.addURI(PROVIDER_NAME, MyDatabaseHelper.TABLE_FAVORITES + "/#", FAVORITE_ID);
    }

    private SQLiteDatabase db;

    public MyContentProvider() {
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        MyDatabaseHelper dbHelper = MyDatabaseHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
        return !(db == null);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        int rowsDeleted = 0;
        switch (uriType) {
            case FAVORITES:
                rowsDeleted = db.delete(MyDatabaseHelper.TABLE_FAVORITES, selection, selectionArgs);
                break;
            case FAVORITE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(MyDatabaseHelper.TABLE_FAVORITES, MyDatabaseHelper.COLUMN_ID + "=" + id, null);
                } else {
                    rowsDeleted = db.delete(MyDatabaseHelper.TABLE_FAVORITES, MyDatabaseHelper.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        long id;
        switch (uriType) {
            case FAVORITES:
                id = db.insert(MyDatabaseHelper.TABLE_FAVORITES, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(MyDatabaseHelper.TABLE_FAVORITES + '/' + id);
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
//        throw new UnsupportedOperationException("Not yet implemented");
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        checkColumns(projection);
        builder.setTables(MyDatabaseHelper.TABLE_FAVORITES);
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case FAVORITES:
                break;
            case FAVORITE_ID:
                builder.appendWhere(MyDatabaseHelper.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        int rowsUpdated = 0;
        switch (uriType) {
            case FAVORITES:
                rowsUpdated = db.update(MyDatabaseHelper.TABLE_FAVORITES, values, selection, selectionArgs);
                break;
            case FAVORITE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(MyDatabaseHelper.TABLE_FAVORITES, values, MyDatabaseHelper.COLUMN_ID + '=' + id, null);
                } else {
                    rowsUpdated = db.update(MyDatabaseHelper.TABLE_FAVORITES, values, MyDatabaseHelper.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = {MyDatabaseHelper.COLUMN_ID,
                MyDatabaseHelper.COLUMN_BACKDROP, MyDatabaseHelper.COLUMN_RELEASE,
                MyDatabaseHelper.COLUMN_RATING, MyDatabaseHelper.COLUMN_PLOT, MyDatabaseHelper.COLUMN_POSTER,
                MyDatabaseHelper.COLUMN_TITLE};
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
