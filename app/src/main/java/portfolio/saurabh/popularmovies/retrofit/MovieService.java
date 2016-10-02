package portfolio.saurabh.popularmovies.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Saurabh on 01-Mar-16.
 */
public interface MovieService {
    @GET("/3/discover/movie")
    Call<MovieList> listMovies(@Query("sort_by") String sort, @Query("api_key") String key);

    @GET("/3/movie/{movie}/videos")
    Call<TrailerList> listTrailers(@Path("movie") String movie, @Query("api_key") String key);

    @GET("/3/movie/{id}/reviews")
    Call<ReviewList> listReviews(@Path("id") String movie, @Query("api_key") String key);
}
