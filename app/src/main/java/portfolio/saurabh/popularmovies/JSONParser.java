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

    public static List<MovieData> parseMovies(String json) throws JSONException, ParseException {
        JSONObject object = new JSONObject(json);
        List<MovieData> movieDataList = new ArrayList<>();
        JSONArray movies = object.getJSONArray("results");
        for (int i = 0; i < movies.length(); i++) {
            movieDataList.add(parseMovie(movies.getJSONObject(i)));
        }
        return movieDataList;
    }

    public static MovieData parseMovie(JSONObject object) throws JSONException, ParseException {
        String title = object.getString("original_title");
        String posterurl = object.getString("poster_path");
        int id = object.getInt("id");
        String backdrop = object.getString("backdrop_path");
        String plot = object.getString("overview");
        double user_rating = object.getDouble("vote_average");
        Date release_date = sdf.parse(object.getString("release_date").equals("null") || object.getString("release_date").isEmpty() ? "01-01-1970" : object.getString("release_date"));
        return new MovieData(title, posterurl, plot, user_rating, release_date, backdrop, id);
    }

    public static List<String> parseTrailers(String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        JSONArray trailers = object.getJSONArray("results");
        List<String> strings = new ArrayList<>(trailers.length());
        for (int i = 0; i < trailers.length(); i++) {
            try {
                strings.add(parseTrailer(trailers.getJSONObject(i)));
            } catch (JSONException ignored) {
            }
        }
        return strings;
    }

    public static String parseTrailer(JSONObject object) throws JSONException {
        if (object.getString("site").equals("YouTube"))
            return object.getString("key");
        else
            throw new JSONException("Source is not YouTube.");
    }
}
