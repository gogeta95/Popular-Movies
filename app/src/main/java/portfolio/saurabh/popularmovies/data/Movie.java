package portfolio.saurabh.popularmovies.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import portfolio.saurabh.popularmovies.database.MyDatabaseHelper;

/**
 * Created by Saurabh on 01-Mar-16.
 */
@Entity(tableName = MyDatabaseHelper.TABLE_FAVORITES)
@TypeConverters(DateConverter.class)
public class Movie implements Parcelable {

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    @SerializedName("title")
    public String title;
    @SerializedName("poster_path")
    public String posterurl;
    @SerializedName("overview")
    public String plot;
    @SerializedName("vote_average")
    public double user_rating;
    @SerializedName("release_date")
    public Date release_date;
    @SerializedName("backdrop_path")
    public String backdrop;
    @SerializedName("id")
    public @PrimaryKey int id;

    public Movie(String title, String posterurl, String plot, double user_rating, Date release_date, String backdrop, int id) {
        this.title = title;
        this.posterurl = posterurl;
        this.plot = plot;
        this.user_rating = user_rating;
        this.release_date = release_date;
        this.backdrop = backdrop;
        this.id = id;
    }

    protected Movie(Parcel in) {
        title = in.readString();
        posterurl = in.readString();
        plot = in.readString();
        user_rating = in.readDouble();
        release_date = new Date(in.readLong());
        backdrop = in.readString();
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(posterurl);
        dest.writeString(plot);
        dest.writeDouble(user_rating);
        dest.writeLong(release_date != null ? release_date.getTime() : 0);
        dest.writeString(backdrop);
        dest.writeInt(id);
    }

    @Override
    public String toString() {
        return "MovieData{" +
                "title='" + title + '\'' +
                ", posterurl='" + posterurl + '\'' +
                ", plot='" + plot + '\'' +
                ", user_rating=" + user_rating +
                ", release_date=" + release_date +
                ", backdrop='" + backdrop + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
