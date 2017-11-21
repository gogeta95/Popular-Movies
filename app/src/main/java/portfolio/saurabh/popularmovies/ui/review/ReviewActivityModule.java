package portfolio.saurabh.popularmovies.ui.review;

import dagger.Module;
import dagger.Provides;

/**
 * Created by saurabh on 21/11/17.
 */
@Module
public class ReviewActivityModule {

    @Provides
    ReviewAdapter provideReviewAdapter(ReviewActivity reviewActivity) {
        return new ReviewAdapter(reviewActivity);
    }
}
