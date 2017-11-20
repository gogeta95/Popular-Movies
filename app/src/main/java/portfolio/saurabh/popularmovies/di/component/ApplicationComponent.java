package portfolio.saurabh.popularmovies.di.component;

import javax.inject.Singleton;

import dagger.Component;
import portfolio.saurabh.popularmovies.di.module.ApplicationModule;
import portfolio.saurabh.popularmovies.ui.detail.DetailsFragment;
import portfolio.saurabh.popularmovies.ui.main.favorite.FavoritesFragment;
import portfolio.saurabh.popularmovies.ui.main.listfragment.ListFragment;

/**
 * Created by saurabh on 20/11/17.
 */

@Component(modules = ApplicationModule.class)
@Singleton
public interface ApplicationComponent {

    void inject(ListFragment fragment);

    void inject(FavoritesFragment fragment);

    void inject(DetailsFragment fragment);
}
