package portfolio.saurabh.popularmovies.listfragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.res.Configuration;
import android.os.Bundle;
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

import java.util.List;

import portfolio.saurabh.popularmovies.R;
import portfolio.saurabh.popularmovies.RecyclerAdapter;
import portfolio.saurabh.popularmovies.data.Movie;
import portfolio.saurabh.popularmovies.database.MyDatabaseHelper;


public class ListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ListFragmentContract.View {
    public static final String KEY_DATA = "DATA";
    public static final String KEY_TITLE = "title";
    private static ListFragmentContract.Presenter presenter;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    ProgressBar progressBar;
    SwipeRefreshLayout refreshLayout;
    private LiveData<List<Movie>> movieLiveData;

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
        movieLiveData = MyDatabaseHelper.getDatabase(getContext()).movieModel().getAllMovies();
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
        if (presenter == null) {
            presenter = new ListFragmentPresenter(getContext());
        }
        presenter.setView(this);
        if (getArguments().getString(KEY_TITLE) != null) {
            presenter.setTitle(getArguments().getString(KEY_TITLE));
        }
        adapter = new RecyclerAdapter(getContext());
        recyclerView.setAdapter(adapter);

        movieLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                adapter.setMovies(movies);
                progressBar.setVisibility(movies.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });

        return layout;
    }

    @Override
    public void onRefresh() {
        if (presenter != null) {
            presenter.refresh();
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.setView(null);
        presenter = null;
    }
}
