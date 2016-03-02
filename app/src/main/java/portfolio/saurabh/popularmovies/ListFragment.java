package portfolio.saurabh.popularmovies;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import portfolio.saurabh.popularmovies.retrofit.MovieList;
import portfolio.saurabh.popularmovies.retrofit.MovieService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final String KEY_DATA = "DATA";
    public static final String KEY_TITLE = "title";
    //    List<MovieData> movieDataList;
    MovieList movieList;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    ProgressBar progressBar;
    SwipeRefreshLayout refreshLayout;

    public ListFragment() {

    }

    public static ListFragment getInstance(String title) {
        ListFragment fragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.list_layout, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = (int) (metrics.widthPixels / metrics.density);
        //For Tabs
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        width = isTablet && isLandscape ? (width / 2) : width;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), width / 140));
        refreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        setHasOptionsMenu(true);
        refreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.progress_colors));
        progressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList(KEY_DATA) != null) {
            movieList.movies = savedInstanceState.getParcelableArrayList(KEY_DATA);
            progressBar.setVisibility(View.GONE);
            adapter = new RecyclerAdapter(getContext(), movieList);
            if (recyclerView.getAdapter() != null) {
                recyclerView.swapAdapter(adapter, false);
            } else {
                recyclerView.setAdapter(adapter);
            }
        } else {
            getData();
        }
        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (movieList != null) {
            outState.putParcelableArrayList(KEY_DATA, (ArrayList<? extends Parcelable>) movieList.movies);
        }
    }

    @Override
    public void onRefresh() {
        getData();
    }

    public void getData() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                try {
                    return format.parse(json.getAsString());
                } catch (ParseException e) {
                    return null;
                }
            }
        }).create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(UriBuilder.BASE).addConverterFactory(GsonConverterFactory.create(gson)).build();
        MovieService service = retrofit.create(MovieService.class);
        Call<MovieList> listCall = service.listMovies(getArguments().getString(KEY_TITLE), getString(R.string.api_key));
        listCall.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                adapter = new RecyclerAdapter(getContext(), response.body());
                if (recyclerView.getAdapter() != null) {
                    recyclerView.swapAdapter(adapter, false);
                } else {
                    recyclerView.setAdapter(adapter);
                }
                refreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                t.printStackTrace();
                Snackbar.make(recyclerView, getString(R.string.connection_error), Snackbar.LENGTH_SHORT).setAction("Try again", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getData();
                    }
                }).show();
            }
        });
    }
}
