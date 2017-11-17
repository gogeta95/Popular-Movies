package portfolio.saurabh.popularmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import portfolio.saurabh.popularmovies.data.Movie;
import portfolio.saurabh.popularmovies.data.MovieDao;
import portfolio.saurabh.popularmovies.data.ReviewData;
import portfolio.saurabh.popularmovies.data.ReviewDataDao;
import portfolio.saurabh.popularmovies.data.Trailer;
import portfolio.saurabh.popularmovies.data.TrailerDao;


@Database(entities = {Movie.class, Trailer.class, ReviewData.class}, version = 1)
public abstract class MyDatabaseHelper extends RoomDatabase {

    private static MyDatabaseHelper INSTANCE = null;

    public static MyDatabaseHelper getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), MyDatabaseHelper.class, "Movie.db")
                            .build();
        }
        return INSTANCE;
    }

    public abstract MovieDao movieModel();

    public abstract TrailerDao trailerModel();

    public abstract ReviewDataDao reviewModel();

}
