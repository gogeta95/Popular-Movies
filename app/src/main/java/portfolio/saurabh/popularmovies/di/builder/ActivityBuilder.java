package portfolio.saurabh.popularmovies.di.builder;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import portfolio.saurabh.popularmovies.ui.detail.DetailFragmentProvider;
import portfolio.saurabh.popularmovies.ui.detail.MovieDetail;
import portfolio.saurabh.popularmovies.ui.main.MainActivity;
import portfolio.saurabh.popularmovies.ui.main.favorite.FavoriteFragmentProvider;
import portfolio.saurabh.popularmovies.ui.main.listfragment.ListFragmentProvider;
import portfolio.saurabh.popularmovies.ui.review.ReviewActivity;
import portfolio.saurabh.popularmovies.ui.review.ReviewActivityModule;

/**
 * Created by saurabh on 21/11/17.
 */

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = DetailFragmentProvider.class)
    abstract MovieDetail bindDetailActivity();

    @ContributesAndroidInjector(modules = ReviewActivityModule.class)
    abstract ReviewActivity bindReviewActivity();

    @ContributesAndroidInjector(modules = {ListFragmentProvider.class, FavoriteFragmentProvider.class})
    abstract MainActivity bindMainActivity();
}
