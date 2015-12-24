package portfolio.saurabh.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;


public class MovieData implements Parcelable {
    public static final Creator<MovieData> CREATOR = new Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };
    public String title;
    public String posterurl;
    public String plot;
    public double user_rating;
    public Date release_date;
    public String backdrop;
    public int id;

    public MovieData(String title, String posterurl, String plot, double user_rating, Date release_date, String backdrop,int id) {
        this.title = title;
        this.posterurl = posterurl;
        this.plot = plot;
        this.user_rating = user_rating;
        this.release_date = release_date;
        this.backdrop=backdrop;
        this.id=id;
    }

    protected MovieData(Parcel in) {
        title = in.readString();
        posterurl = in.readString();
        plot = in.readString();
        user_rating = in.readDouble();
        release_date=new Date(in.readLong());
        backdrop=in.readString();
        id=in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(posterurl);
        dest.writeString(plot);
        dest.writeDouble(user_rating);
        dest.writeLong(release_date.getTime());
        dest.writeString(backdrop);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
