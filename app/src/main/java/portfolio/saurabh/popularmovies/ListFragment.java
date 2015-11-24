package portfolio.saurabh.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final String KEY_DATA = "DATA";
    public static final String KEY_TITLE="title";
    List<MovieData> movieDataList;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    ProgressBar progressBar;
    SwipeRefreshLayout refreshLayout;

    public  ListFragment(){

    }

    public static ListFragment getInstance(String title) {
        ListFragment fragment= new ListFragment();
        Bundle bundle= new Bundle();
        bundle.putString(KEY_TITLE,title);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.list_layout, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        refreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.progress_colors));
        progressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList(KEY_DATA) != null) {
            movieDataList = savedInstanceState.getParcelableArrayList(KEY_DATA);
            progressBar.setVisibility(View.GONE);
            adapter = new RecyclerAdapter(getContext(), movieDataList);
            if (recyclerView.getAdapter() != null) {
                recyclerView.swapAdapter(adapter, false);
            } else {
                recyclerView.setAdapter(adapter);
            }
        } else {
            new GetMoviesTask().execute(UriBuilder.MOST_POPULAR);
        }
        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (movieDataList != null) {
            outState.putParcelableArrayList(KEY_DATA, (ArrayList<? extends Parcelable>) movieDataList);
        }
    }

    @Override
    public void onRefresh() {
        new GetMoviesTask().execute(UriBuilder.MOST_POPULAR);
    }

    class GetMoviesTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                UriBuilder uri = new UriBuilder(getContext());
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
                movieDataList = JSONParser.parse(sb.toString());
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
                Toast.makeText(getContext(), "There was an error fetching data. Please try again later.", Toast.LENGTH_LONG).show();
            } else {
                adapter = new RecyclerAdapter(getContext(), movieDataList);
                if (recyclerView.getAdapter() != null) {
                    recyclerView.swapAdapter(adapter, true);
                } else {
                    recyclerView.setAdapter(adapter);
                }
                refreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }
}
