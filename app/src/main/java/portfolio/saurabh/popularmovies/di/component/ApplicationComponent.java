package portfolio.saurabh.popularmovies.di.component;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import portfolio.saurabh.popularmovies.MovieApplication;
import portfolio.saurabh.popularmovies.data.MovieService;
import portfolio.saurabh.popularmovies.database.MyDatabaseHelper;
import portfolio.saurabh.popularmovies.di.builder.ActivityBuilder;
import portfolio.saurabh.popularmovies.di.module.ApplicationModule;

/**
 * Created by saurabh on 20/11/17.
 */

@Component(modules = {ApplicationModule.class, AndroidInjectionModule.class, ActivityBuilder.class})
@Singleton
public interface ApplicationComponent {


    void inject(MovieApplication movieApplication);

    MyDatabaseHelper dbHelper();

    MovieService movieService();

    String apikey();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        ApplicationComponent build();
    }
}
