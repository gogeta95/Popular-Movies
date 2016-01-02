package portfolio.saurabh.popularmovies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

import portfolio.saurabh.popularmovies.MovieData;


public class FavoritesDataSource {
    private SQLiteDatabase database;
    private MyDatabaseHelper dbHelper;

    public FavoritesDataSource(Context context) {
        dbHelper = new MyDatabaseHelper(context);
    }

    public void open(boolean readonly) {
        database = readonly ? dbHelper.getReadableDatabase() : dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    private MovieData cursorToMovie(Cursor cursor) {
        int id = cursor.getInt(0);
        String title = cursor.getString(1);
        String poster = cursor.getString(2);
        String plot = cursor.getString(3);
        double rating = cursor.getDouble(4);
        Date release = new Date(cursor.getLong(5));
        String backdrop = cursor.getString(6);
        return new MovieData(title, poster, plot, rating, release, backdrop, id);
    }

    public long insertMovie(MovieData movie) {
        ContentValues values = new ContentValues();
        values.put(MyDatabaseHelper.COLUMN_ID, movie.id);
        values.put(MyDatabaseHelper.COLUMN_TITLE, movie.title);
        values.put(MyDatabaseHelper.COLUMN_POSTER, movie.posterurl);
        values.put(MyDatabaseHelper.COLUMN_PLOT, movie.plot);
        values.put(MyDatabaseHelper.COLUMN_RATING, movie.user_rating);
        values.put(MyDatabaseHelper.COLUMN_RELEASE, movie.release_date.getTime());
        values.put(MyDatabaseHelper.COLUMN_BACKDROP, movie.backdrop);
        return database.insert(MyDatabaseHelper.TABLE_FAVORITES, null, values);
    }

    public boolean isMovieExists(int id) {
        String query = "select 1 from " + MyDatabaseHelper.TABLE_FAVORITES + " where " + MyDatabaseHelper.COLUMN_ID + " = " + id;
        Cursor c = database.rawQuery(query, null);
        boolean b = c.moveToFirst();
        c.close();
        return b;
    }

    public int removeMovie(int id) {
       return database.delete(MyDatabaseHelper.TABLE_FAVORITES, MyDatabaseHelper.COLUMN_ID + " = " + id, null);
    }
    public Cursor getAllMovies(){
        return database.rawQuery("select * from "+MyDatabaseHelper.TABLE_FAVORITES,null);
    }
}
