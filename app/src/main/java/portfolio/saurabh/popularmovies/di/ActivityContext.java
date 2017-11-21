package portfolio.saurabh.popularmovies.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by saurabh on 21/11/17.
 */

@Qualifier
@Retention(RetentionPolicy.SOURCE)
public @interface ActivityContext {
}
