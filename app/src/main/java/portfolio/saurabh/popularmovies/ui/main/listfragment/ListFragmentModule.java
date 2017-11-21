package portfolio.saurabh.popularmovies.ui.main.listfragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by saurabh on 21/11/17.
 */

@Module
public class ListFragmentModule {

    @Provides
    ListFragmentContract.Presenter providePresenter(ListFragmentPresenter listFragmentPresenter) {
        return listFragmentPresenter;
    }


}
