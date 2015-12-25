package portfolio.saurabh.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Saurabh on 12/25/2015.
 */
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
    public String user;
    public String review;
    public String url;

    protected ReviewData(Parcel in) {
        user = in.readString();
        review = in.readString();
        url = in.readString();
    }

    public ReviewData(String user, String review, String url) {
        this.user = user;
        this.review = review;
        this.url = url;
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
    }
}
