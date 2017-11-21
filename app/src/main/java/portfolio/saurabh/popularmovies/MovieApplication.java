package portfolio.saurabh.popularmovies;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import portfolio.saurabh.popularmovies.di.component.ApplicationComponent;
import portfolio.saurabh.popularmovies.di.component.DaggerApplicationComponent;

/**
 * Created by saurabh on 20/11/17.
 */

public class MovieApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .application(this)
                .build();

        component.inject(this);
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
