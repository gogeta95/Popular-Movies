package portfolio.saurabh.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Saurabh on 01-Mar-16.
 */
public class MovieList implements Parcelable {
    public static final Creator<MovieList> CREATOR = new Creator<MovieList>() {
        @Override
        public MovieList createFromParcel(Parcel in) {
            return new MovieList(in);
        }

        @Override
        public MovieList[] newArray(int size) {
            return new MovieList[size];
        }
    };
    @SerializedName("results")
    public List<Movie> movies;

    protected MovieList(Parcel in) {
        movies = in.createTypedArrayList(Movie.CREATOR);
    }

    @Override
    public String toString() {
        return movies.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(movies);
    }
}
