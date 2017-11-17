package portfolio.saurabh.popularmovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import portfolio.saurabh.popularmovies.database.MyDatabaseHelper;

/**
 * Created by saurabh on 17/11/17.
 */

@Dao
public interface MovieDao {

    @Query("SELECT * FROM "+ MyDatabaseHelper.TABLE_FAVORITES)
    LiveData<List<Movie>> getMovies();

    @Query("SELECT 1 FROM "+ MyDatabaseHelper.TABLE_FAVORITES+" WHERE id = :id")
    boolean isMovieExists(int id);

    @Query("DELETE FROM "+ MyDatabaseHelper.TABLE_FAVORITES+" WHERE id = :id")
    void deleteMovie(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);
}
