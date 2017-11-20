package portfolio.saurabh.popularmovies.di.module;

import android.content.Context;
import android.view.LayoutInflater;

import dagger.Module;
import dagger.Provides;
import portfolio.saurabh.popularmovies.di.UIScope;
import portfolio.saurabh.popularmovies.ui.main.listfragment.ListFragmentContract;
import portfolio.saurabh.popularmovies.ui.main.listfragment.ListFragmentPresenter;

/**
 * Created by saurabh on 20/11/17.
 */


@Module
public class UiModule {
    private Context context;

    public UiModule(Context context) {
        this.context = context;
    }

    @Provides
    @UIScope
    Context provideContext() {
        return context;
    }

    @Provides
    @UIScope
    LayoutInflater provideLayoutInflater() {
        return LayoutInflater.from(context);
    }

    @Provides
    @UIScope
    ListFragmentContract.Presenter provideListPresenter(ListFragmentPresenter listFragmentPresenter) {
        return listFragmentPresenter;
    }
}
