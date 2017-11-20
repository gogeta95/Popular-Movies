package portfolio.saurabh.popularmovies.di.component;

import dagger.Component;
import portfolio.saurabh.popularmovies.di.UIScope;
import portfolio.saurabh.popularmovies.di.module.UiModule;
import portfolio.saurabh.popularmovies.ui.main.listfragment.ListFragment;

/**
 * Created by saurabh on 20/11/17.
 */

@Component(dependencies = ApplicationComponent.class, modules = UiModule.class)
@UIScope
public interface UiComponent {

    void inject(ListFragment listFragment);
}
