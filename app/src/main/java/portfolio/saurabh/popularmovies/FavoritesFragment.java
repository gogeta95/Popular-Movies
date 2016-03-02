package portfolio.saurabh.popularmovies;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import portfolio.saurabh.popularmovies.database.CursorAdapter;
import portfolio.saurabh.popularmovies.database.FavoritesDataSource;


public class FavoritesFragment extends Fragment {
    RecyclerView recyclerView;
    FavoritesDataSource dataSource;
    Cursor cursor;
    private CursorAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSource = new FavoritesDataSource(getContext());
        dataSource.open(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.favorites_list, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = (int) (metrics.widthPixels / metrics.density);
        //For Tabs
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        width = isTablet && isLandscape ? (width / 2) : width;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), width / 140));
        cursor = dataSource.getAllMovies();
        mAdapter = new CursorAdapter(getContext(), cursor);
        recyclerView.setAdapter(mAdapter);
        return layout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }

    @Override
    public void onStart() {
        super.onStart();
        cursor = dataSource.getAllMovies();
        mAdapter.swapCursor(cursor);
    }
}
