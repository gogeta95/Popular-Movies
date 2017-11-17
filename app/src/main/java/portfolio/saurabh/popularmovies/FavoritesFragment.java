package portfolio.saurabh.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import portfolio.saurabh.popularmovies.data.Movie;
import portfolio.saurabh.popularmovies.database.CursorAdapter;
import portfolio.saurabh.popularmovies.database.MyDatabaseHelper;


public class FavoritesFragment extends Fragment {
    RecyclerView recyclerView;
    private CursorAdapter mAdapter;
    private LiveData<List<Movie>> movieLiveData;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.favorites_list, container, false);

        recyclerView = layout.findViewById(R.id.recycler_view);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = (int) (metrics.widthPixels / metrics.density);
        //For Tabs
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        width = isTablet && isLandscape ? (width / 2) : width;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), width / 140));
        mAdapter = new CursorAdapter(getContext());
        recyclerView.setAdapter(mAdapter);

        movieLiveData = MyDatabaseHelper.getDatabase(getContext()).movieModel().getMovies();
        movieLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mAdapter.setData(movies);

                if (MainActivity.mIsDualPane) {

                    if (getActivity() != null && movies != null && !movies.isEmpty()) {
                        final Movie movie = movies.get(0);
                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.movie_detail, DetailsFragment.getInstance(movie))
                                .commit();
                    }
                }
            }
        });

        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        movieLiveData.removeObservers(this);
    }
}
