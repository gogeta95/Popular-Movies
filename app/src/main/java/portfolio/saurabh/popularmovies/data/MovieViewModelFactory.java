package portfolio.saurabh.popularmovies.data;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class MovieViewModelFactory implements ViewModelProvider.Factory {

    private MovieRepository mMovieRepository;


    public MovieViewModelFactory(@NonNull MovieRepository movieRepository) {
        mMovieRepository = movieRepository;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieListViewModel(mMovieRepository);
    }
}
