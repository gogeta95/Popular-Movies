package portfolio.saurabh.popularmovies.data;

import android.arch.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private static MovieRepository movieRepository;

    private final MovieService movieService;
    private final MovieDao movieDao;

    private MovieRepository(MovieService movieService, MovieDao movieDao) {
        this.movieService = movieService;
        this.movieDao = movieDao;
    }

    public static MovieRepository getInstance(MovieService movieService, MovieDao movieDao) {
        if (movieRepository == null) {
            movieRepository = new MovieRepository(movieService, movieDao);
        }
        return movieRepository;
    }

    public LiveData<List<Movie>> getAllMovies() {
        return movieDao.getAllMovies();
    }

    public void refreshMovies(String sort, String key) {
        movieService.listMovies(sort, key)
                .enqueue(new Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, final Response<MovieList> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Completable.fromCallable(new Callable<Object>() {
                                @Override
                                public Object call() throws Exception {
                                    movieDao.insertAll(response.body().movies);
                                    return null;
                                }
                            })
                                    .subscribeOn(Schedulers.io())
                                    .subscribe();

                        }
                    }

                    @Override
                    public void onFailure(Call<MovieList> call, Throwable t) {

                    }
                });
    }
}
