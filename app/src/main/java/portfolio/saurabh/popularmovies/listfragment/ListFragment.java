package portfolio.saurabh.popularmovies.listfragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import portfolio.saurabh.popularmovies.R;
import portfolio.saurabh.popularmovies.RecyclerAdapter;
import portfolio.saurabh.popularmovies.UriBuilder;
import portfolio.saurabh.popularmovies.data.Movie;
import portfolio.saurabh.popularmovies.data.MovieDao;
import portfolio.saurabh.popularmovies.data.MovieListViewModel;
import portfolio.saurabh.popularmovies.data.MovieRepository;
import portfolio.saurabh.popularmovies.data.MovieService;
import portfolio.saurabh.popularmovies.data.MovieViewModelFactory;
import portfolio.saurabh.popularmovies.database.MyDatabaseHelper;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ListFragmentContract.View {

    private static final String TAG = "ListFragment";
    public static final String KEY_DATA = "DATA";
    public static final String KEY_TITLE = "title";
    MovieListViewModel movieListViewModel;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.list_layout, container, false);
        recyclerView = layout.findViewById(R.id.recycler_view);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = (int) (metrics.widthPixels / metrics.density);
        //For Tabs
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        width = isTablet && isLandscape ? (width / 2) : width;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), width / 140));
        refreshLayout = layout.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        setHasOptionsMenu(true);
        refreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.progress_colors));
        progressBar = layout.findViewById(R.id.progressBar);
        adapter = new RecyclerAdapter(getContext());
        recyclerView.setAdapter(adapter);

        setupViewModel();

        return layout;
    }

    private void setupViewModel() {
//USE DI HERE
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
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UriBuilder.BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        MovieService service = retrofit.create(MovieService.class);
        MovieDao movieDao = MyDatabaseHelper.getDatabase(getContext()).movieModel();
        MovieRepository movieRepository = MovieRepository.getInstance(service, movieDao);
        MovieViewModelFactory factory = new MovieViewModelFactory(movieRepository);
        movieListViewModel = ViewModelProviders.of(this, factory).get(MovieListViewModel.class);
        movieListViewModel.getMovieListObservable().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                updateUI(movies);
            }
        });

        if (getArguments().getString(KEY_TITLE) != null) {
            movieListViewModel.setKey(getString(R.string.api_key));
            movieListViewModel.setTitle(getArguments().getString(KEY_TITLE));
        }
    }

    private void updateUI(List<Movie> movies) {
        Log.d(TAG, "updateUI: "+movies);
        adapter.setMovies(movies);
        progressBar.setVisibility(movies.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onRefresh() {
        movieListViewModel.refresh();
    }


    @Override
    public void showErrorInSnackbar(String message) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT).setAction("Try again", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        }).show();
    }
}
