package portfolio.saurabh.popularmovies.listfragment;

/**
 * Created by saurabh on 3/10/16.
 */

public class ListFragmentContract {
    public interface View {

        void showErrorInSnackbar(String message);

    }

    public interface Presenter {
        void refresh();

        void setView(View view);

        void setTitle(String title);
    }
}
