package portfolio.saurabh.popularmovies.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import portfolio.saurabh.popularmovies.Trailer;

/**
 * Created by Saurabh on 02-Mar-16.
 */
public class TrailerList {
    @SerializedName("results")
    public List<Trailer> trailers;

    @Override
    public String toString() {
        return trailers.toString();
    }
}
