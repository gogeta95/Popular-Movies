package portfolio.saurabh.popularmovies.ui.main.favorite;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by saurabh on 21/11/17.
 */

@Module
public abstract class FavoriteFragmentProvider {

    @ContributesAndroidInjector(modules = FavoriteFragmentModule.class)
    abstract FavoritesFragment bindFavoriteFragment();
}
