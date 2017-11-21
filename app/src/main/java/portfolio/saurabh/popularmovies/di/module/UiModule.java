package portfolio.saurabh.popularmovies.di.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import portfolio.saurabh.popularmovies.di.ActivityContext;
import portfolio.saurabh.popularmovies.di.UIScope;
import portfolio.saurabh.popularmovies.ui.main.listfragment.ListFragmentContract;
import portfolio.saurabh.popularmovies.ui.main.listfragment.ListFragmentPresenter;

/**
 * Created by saurabh on 20/11/17.
 */


@Module
public class UiModule {

    @Provides
    @UIScope
    Context provideContext(@ActivityContext Context context) {
        return context;
    }

    @Provides
    @UIScope
    ListFragmentContract.Presenter provideListPresenter(ListFragmentPresenter listFragmentPresenter) {
        return listFragmentPresenter;
    }
}
