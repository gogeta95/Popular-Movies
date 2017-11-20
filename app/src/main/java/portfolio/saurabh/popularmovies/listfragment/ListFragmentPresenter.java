package portfolio.saurabh.popularmovies.listfragment;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import portfolio.saurabh.popularmovies.R;
import portfolio.saurabh.popularmovies.data.MovieDao;
import portfolio.saurabh.popularmovies.data.MovieList;
import portfolio.saurabh.popularmovies.data.MovieService;
import portfolio.saurabh.popularmovies.database.MyDatabaseHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by saurabh on 3/10/16.
 */

public class ListFragmentPresenter implements ListFragmentContract.Presenter {
    private static final String TAG = ListFragmentPresenter.class.getSimpleName();
    private final MovieService service;
    private final String apiKey;
    private final MovieDao movieDao;
    private ListFragmentContract.View view;
    private String title;

    @Inject
    ListFragmentPresenter(MovieService service, MyDatabaseHelper myDatabaseHelper, String apiKey) {
        movieDao = myDatabaseHelper.movieModel();
        this.apiKey = apiKey;
        this.service = service;

    }


    @Override
    public void refresh() {
        if (view != null) {
            Call<MovieList> listCall = service.listMovies(title, apiKey);
            listCall.enqueue(new Callback<MovieList>() {
                @Override
                public void onResponse(Call<MovieList> call, final Response<MovieList> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Completable.fromCallable(new Callable<Object>() {
                            @Override
                            public Object call() throws Exception {
                                movieDao.insertAll(response.body().movies);

                                Log.d(TAG, "call: " + response.body());
                                return null;
                            }
                        })
                                .subscribeOn(Schedulers.io())
                                .subscribe();

                    } else {
                        if (view != null) {
                            view.showErrorInSnackbar(R.string.connection_error);
                        }
                    }
                }

                @Override
                public void onFailure(Call<MovieList> call, Throwable t) {
                    t.printStackTrace();
                    if (view != null) {
                        view.showErrorInSnackbar(R.string.connection_error);
                    }
                }
            });
        }
    }

    @Override
    public void setView(ListFragmentContract.View view) {
        this.view = view;
        refresh();
    }

    @Override
    public void setTitle(@NonNull String title) {
        this.title = title;
    }

}
