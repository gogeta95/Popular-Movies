package portfolio.saurabh.popularmovies.di.builder;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import portfolio.saurabh.popularmovies.ui.detail.DetailFragmentProvider;
import portfolio.saurabh.popularmovies.ui.detail.MovieDetail;

/**
 * Created by saurabh on 21/11/17.
 */

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {DetailFragmentProvider.class})
    abstract MovieDetail bindDetailActivity();
}
