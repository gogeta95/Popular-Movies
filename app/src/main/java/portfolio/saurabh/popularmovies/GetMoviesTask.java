package portfolio.saurabh.popularmovies;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Scanner;


public class GetMoviesTask extends AsyncTask<String, Void, Integer> {
    private ListFragment listFragment;

    public GetMoviesTask(ListFragment listFragment) {
        this.listFragment = listFragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listFragment.progressBar.setVisibility(View.VISIBLE);
        listFragment.recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected Integer doInBackground(String... params) {
        try {
            UriBuilder uri = new UriBuilder(listFragment.getContext(),UriBuilder.BASE_URL);
            uri.setSortValue(params[0]);
            URL url = new URL(uri.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            StringBuilder sb = new StringBuilder();
            Scanner sc = new Scanner(connection.getInputStream());
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine());
            }
            connection.disconnect();
            listFragment.movieDataList = JSONParser.parseMovies(sb.toString());
            return 0;
        } catch (IOException | ParseException | JSONException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return 1;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if (integer == 1) {
            Snackbar.make(listFragment.getView(),listFragment.getString(R.string.connection_error),Snackbar.LENGTH_SHORT).setAction("Try again", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listFragment.onRefresh();
                }
            }).show();
        } else {
            listFragment.adapter = new RecyclerAdapter(listFragment.getContext(), listFragment.movieDataList);
            if (listFragment.recyclerView.getAdapter() != null) {
                listFragment.recyclerView.swapAdapter(listFragment.adapter, false);
            } else {
                listFragment.recyclerView.setAdapter(listFragment.adapter);
            }
            listFragment.refreshLayout.setRefreshing(false);
            listFragment.progressBar.setVisibility(View.GONE);
            listFragment.recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
