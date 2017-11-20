package portfolio.saurabh.popularmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import portfolio.saurabh.popularmovies.data.Movie;
import portfolio.saurabh.popularmovies.data.MovieDao;
import portfolio.saurabh.popularmovies.data.ReviewData;
import portfolio.saurabh.popularmovies.data.ReviewDataDao;
import portfolio.saurabh.popularmovies.data.Trailer;
import portfolio.saurabh.popularmovies.data.TrailerDao;


@Database(entities = {Movie.class, Trailer.class, ReviewData.class}, version = 1)
public abstract class MyDatabaseHelper extends RoomDatabase {

    public abstract MovieDao movieModel();

    public abstract TrailerDao trailerModel();

    public abstract ReviewDataDao reviewModel();

}
