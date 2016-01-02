package portfolio.saurabh.popularmovies;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import portfolio.saurabh.popularmovies.database.CursorAdapter;
import portfolio.saurabh.popularmovies.database.FavoritesDataSource;


public class FavoritesFragment extends Fragment {
    RecyclerView recyclerView;
    FavoritesDataSource dataSource;

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
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
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
        recyclerView.setAdapter(new CursorAdapter(getContext(), dataSource.getAllMovies()));
    }
}
