package portfolio.saurabh.popularmovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by saurabh on 17/11/17.
 */

@Dao
public interface MovieDao {

    @Query("SELECT * FROM Movie")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM Movie WHERE favorite = 1")
    LiveData<List<Movie>> getAllFavoriteMovies();

    @Query("UPDATE Movie SET favorite = :favorite " +
            "WHERE id=:id")
    void setFavorite(int id, boolean favorite);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Movie> movies);
}
