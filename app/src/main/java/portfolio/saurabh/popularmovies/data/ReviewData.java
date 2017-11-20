package portfolio.saurabh.popularmovies.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Saurabh on 12/25/2015.
 */
@Entity
public class ReviewData implements Parcelable {
    public static final Creator<ReviewData> CREATOR = new Creator<ReviewData>() {
        @Override
        public ReviewData createFromParcel(Parcel in) {
            return new ReviewData(in);
        }

        @Override
        public ReviewData[] newArray(int size) {
            return new ReviewData[size];
        }
    };
    @SerializedName("author")
    public String user;
    @SerializedName("content")
    public String review;
    @SerializedName("url")
    public @PrimaryKey
    @NonNull
    String url;

    public int movieId;

    public ReviewData() {
    }

    protected ReviewData(Parcel in) {
        user = in.readString();
        review = in.readString();
        url = in.readString();
        movieId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user);
        dest.writeString(review);
        dest.writeString(url);
        dest.writeInt(movieId);
    }
}
