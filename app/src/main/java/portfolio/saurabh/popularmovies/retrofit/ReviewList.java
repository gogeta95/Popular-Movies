package portfolio.saurabh.popularmovies.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import portfolio.saurabh.popularmovies.ReviewData;

/**
 * Created by Saurabh on 02-Mar-16.
 */
public class ReviewList {
    @SerializedName("results")
    public List<ReviewData> reviewList;
}
