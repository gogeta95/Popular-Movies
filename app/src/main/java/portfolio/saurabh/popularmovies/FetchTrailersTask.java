package portfolio.saurabh.popularmovies;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Saurabh on 12/24/2015.
 */
public class FetchTrailersTask extends AsyncTask<Integer, Void, Void> {
    MovieDetail movieDetail;
    List<String> trailer_thumbs;

    public FetchTrailersTask(MovieDetail movieDetail) {
        this.movieDetail = movieDetail;
    }


    @Override
    protected Void doInBackground(Integer... params) {
        UriBuilder uri = new UriBuilder(movieDetail, UriBuilder.BASE_URL_TRAILERS + params[0] + "/videos");
        try {
            URL url = new URL(uri.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            StringBuilder sb = new StringBuilder();
            Scanner sc = new Scanner(connection.getInputStream());
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine());
            }
            connection.disconnect();
            trailer_thumbs = JSONParser.parseTrailers(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        movieDetail.pager.setAdapter(new TrailerPagerAdapter(movieDetail.getSupportFragmentManager(), trailer_thumbs));
        movieDetail.indicator.setViewPager(movieDetail.pager);
    }
}
