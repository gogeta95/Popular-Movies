package portfolio.saurabh.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MyDatabaseHelper extends SQLiteOpenHelper {
    static final String TABLE_FAVORITES = "favorites";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_TITLE = "title";
    static final String COLUMN_POSTER = "posterurl";
    static final String COLUMN_PLOT = "plot";
    static final String COLUMN_RATING = "user_rating";
    static final String COLUMN_RELEASE = "release_date";
    static final String COLUMN_BACKDROP = "backdrop";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "Movies.db";
    static final String DATABASE_CREATE = "create table "
            + TABLE_FAVORITES + "(" +
            COLUMN_ID + " integer primary key, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_POSTER + " text not null, "
            + COLUMN_PLOT + " text not null, "
            + COLUMN_RATING + " real not null, "
            + COLUMN_RELEASE + " integer not null, "
            + COLUMN_BACKDROP + " text not null);";
    private static MyDatabaseHelper mInstance = null;

    private MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static MyDatabaseHelper getInstance(Context context) {
        if (mInstance == null)
            return new MyDatabaseHelper(context.getApplicationContext());
        else return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MyDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }
}
