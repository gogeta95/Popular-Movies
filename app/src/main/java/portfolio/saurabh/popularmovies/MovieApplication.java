package portfolio.saurabh.popularmovies;

import android.app.Application;

import portfolio.saurabh.popularmovies.di.component.ApplicationComponent;
import portfolio.saurabh.popularmovies.di.component.DaggerApplicationComponent;
import portfolio.saurabh.popularmovies.di.module.ApplicationModule;

/**
 * Created by saurabh on 20/11/17.
 */

public class MovieApplication extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new
                        ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
