package portfolio.saurabh.popularmovies.listfragment;

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

import java.util.ArrayList;

import portfolio.saurabh.popularmovies.R;
import portfolio.saurabh.popularmovies.RecyclerAdapter;
import portfolio.saurabh.popularmovies.retrofit.MovieList;


public class ListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ListFragmentContract.View {
    public static final String KEY_DATA = "DATA";
    public static final String KEY_TITLE = "title";
    private static ListFragmentContract.Presenter presenter;
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
        if (presenter == null) {
            presenter = new ListFragmentPresenter(getContext());
        }
        presenter.setView(this);
        if (getArguments().getString(KEY_TITLE) != null) {
            presenter.setTitle(getArguments().getString(KEY_TITLE));
        }
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
            if (presenter != null) {
                presenter.refresh();
            }
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
        if (presenter != null) {
            presenter.refresh();
        }
    }

    @Override
    public void showList(MovieList movieList) {
        adapter = new RecyclerAdapter(getContext(), movieList);
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
    public void showErrorInSnackbar(String message) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT).setAction("Try again", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        }).show();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter!=null)
            presenter.setView(null);
        presenter=null;
    }
}
