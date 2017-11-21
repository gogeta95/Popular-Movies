package portfolio.saurabh.popularmovies.ui.main.favorite;

import dagger.Module;
import dagger.Provides;

/**
 * Created by saurabh on 21/11/17.
 */

@Module
public class FavoriteFragmentModule {

    @Provides
    ListAdapter provideListAdapter(FavoritesFragment favoritesFragment) {
        return new ListAdapter(favoritesFragment.getContext());
    }
}
