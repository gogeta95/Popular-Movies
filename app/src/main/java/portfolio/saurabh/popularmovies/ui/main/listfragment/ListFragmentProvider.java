package portfolio.saurabh.popularmovies.ui.main.listfragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by saurabh on 21/11/17.
 */

@Module
public abstract class ListFragmentProvider {

    @ContributesAndroidInjector(modules = ListFragmentModule.class)
    abstract ListFragment bindListFragment();
}
