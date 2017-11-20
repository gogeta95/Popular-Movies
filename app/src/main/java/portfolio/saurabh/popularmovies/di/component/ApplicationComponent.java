package portfolio.saurabh.popularmovies.di.component;

import javax.inject.Singleton;

import dagger.Component;
import portfolio.saurabh.popularmovies.data.MovieService;
import portfolio.saurabh.popularmovies.database.MyDatabaseHelper;
import portfolio.saurabh.popularmovies.di.module.ApplicationModule;

/**
 * Created by saurabh on 20/11/17.
 */

@Component(modules = ApplicationModule.class)
@Singleton
public interface ApplicationComponent {


    MyDatabaseHelper dbHelper();

    MovieService movieService();

    String apikey();
}
