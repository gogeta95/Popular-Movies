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
public interface TrailerDao {

    @Query("SELECT * FROM Trailer WHERE movieId = :movieId")
    LiveData<List<Trailer>> getTrailersForMovie(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Trailer> trailers);

}
