package portfolio.saurabh.popularmovies.listfragment;

import portfolio.saurabh.popularmovies.data.MovieList;

/**
 * Created by saurabh on 3/10/16.
 */

public class ListFragmentContract {
    public interface View {
        void showList(MovieList movieList);

        void showErrorInSnackbar(String message);

        void showProgressBar();

    }

    public interface Presenter {
        void refresh();

        void setView(View view);

        void setTitle(String title);
    }
}
