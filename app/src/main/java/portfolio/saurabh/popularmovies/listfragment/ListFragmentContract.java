package portfolio.saurabh.popularmovies.listfragment;

import android.support.annotation.StringRes;

/**
 * Created by saurabh on 3/10/16.
 */

public class ListFragmentContract {
    public interface View {

        void showErrorInSnackbar(@StringRes int resId);
    }

    public interface Presenter {
        void refresh();

        void setView(View view);

        void setTitle(String title);
    }
}
