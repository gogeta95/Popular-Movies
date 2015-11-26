package portfolio.saurabh.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class JSONParser {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public static List<MovieData> parse(String json) throws JSONException, ParseException {
        JSONObject object = new JSONObject(json);
        List<MovieData> movieDataList = new ArrayList<>();
        JSONArray movies = object.getJSONArray("results");
        for (int i = 0; i < movies.length(); i++) {
            movieDataList.add(parsemovie(movies.getJSONObject(i)));
        }
        return movieDataList;
    }

    public static MovieData parsemovie(JSONObject object) throws JSONException, ParseException {
        String title = object.getString("original_title");
        String posterurl = object.getString("poster_path");
        String plot = object.getString("overview");
        double user_rating = object.getDouble("vote_average");
        Date release_date = sdf.parse(object.getString("release_date").equals("null")||object.getString("release_date").isEmpty()?"01-01-1970":object.getString("release_date"));
        return new MovieData(title, posterurl, plot, user_rating, release_date);
    }
}
