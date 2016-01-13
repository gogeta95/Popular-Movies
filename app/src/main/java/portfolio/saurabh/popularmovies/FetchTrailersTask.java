package portfolio.saurabh.popularmovies;

import android.content.Intent;
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
            //Stores Video keys
            trailer_thumbs = JSONParser.parseTrailers(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (!trailer_thumbs.isEmpty()) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out " + detailsFragment.movie.title + "! https://www.youtube.com/watch?v=" + trailer_thumbs.get(0));
            shareIntent.setType("text/plain");
            detailsFragment.shareActionProvider.setShareIntent(shareIntent);
        }
        detailsFragment.pager.setAdapter(new TrailerPagerAdapter(detailsFragment.getChildFragmentManager(), trailer_thumbs));
        detailsFragment.indicator.setViewPager(detailsFragment.pager);
//        Log.d("async","done");
    }
}
