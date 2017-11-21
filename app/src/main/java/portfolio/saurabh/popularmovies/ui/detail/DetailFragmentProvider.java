package portfolio.saurabh.popularmovies.ui.detail;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by saurabh on 21/11/17.
 */

@Module
public abstract class DetailFragmentProvider {

    @ContributesAndroidInjector()
    abstract DetailsFragment bindDetailsFragment();
}
