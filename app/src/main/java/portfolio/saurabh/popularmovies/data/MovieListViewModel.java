package portfolio.saurabh.popularmovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    private final MovieRepository movieRepository;
    private String title;
    private String key;

    public MovieListViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public LiveData<List<Movie>> getMovieListObservable() {
        return movieRepository.getAllMovies();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTitle(String title) {
        this.title = title;
        refresh();
    }

    public void refresh() {
        movieRepository.refreshMovies(title, key);
    }
}
