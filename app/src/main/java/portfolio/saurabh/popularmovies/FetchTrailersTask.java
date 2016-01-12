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
    DetailsFragment detailsFragment;
    List<String> trailer_thumbs;

    public FetchTrailersTask(DetailsFragment detailsFragment) {
        this.detailsFragment=detailsFragment;
    }


    @Override
    protected Void doInBackground(Integer... params) {
        UriBuilder uri = new UriBuilder(detailsFragment.getContext(), UriBuilder.BASE_URL_TRAILERS + params[0] + "/videos");
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
        detailsFragment.pager.setAdapter(new TrailerPagerAdapter(detailsFragment.getActivity().getSupportFragmentManager(), trailer_thumbs));
        detailsFragment.indicator.setViewPager(detailsFragment.pager);
//        Log.d("async","done");
    }
}
