package portfolio.saurabh.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.widget.IconTextView;
import com.squareup.picasso.Picasso;

import me.relex.circleindicator.CircleIndicator;
import portfolio.saurabh.popularmovies.database.FavoritesDataSource;

/**
 * Created by Saurabh on 1/3/2016.
 */
public class DetailsFragment extends Fragment {
    public static final String TAG = "MovieDetail";
    public static final String KEY_MOVIE = "MOVIE";
    MovieData movie;
    ViewPager pager;
    CircleIndicator indicator;
    FavoritesDataSource dataSource;
    FloatingActionButton fab;

    public static DetailsFragment getInstance(Parcelable movie) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_MOVIE, movie);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.movie_detail_fragment, container, false);
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        if (getArguments() != null && getArguments().getParcelable(KEY_MOVIE) != null) {
            movie = getArguments().getParcelable(KEY_MOVIE);
            ImageView poster = (ImageView) layout.findViewById(R.id.poster);
            Picasso.with(getActivity()).load(RecyclerAdapter.POSTER_BASE_URL + movie.posterurl).error(R.drawable.placeholder).into(poster);
            pager = (ViewPager) layout.findViewById(R.id.pager);
            indicator = (CircleIndicator) layout.findViewById(R.id.indicator);
            new FetchTrailersTask(this).execute(movie.id);
            final TextView date = (TextView) layout.findViewById(R.id.date);
            date.setText("In theatres " + DateConvert.convert(movie.release_date));
            final IconTextView rating = (IconTextView) layout.findViewById(R.id.rating);
            rating.setText("{fa-star} " + movie.user_rating + "/10");
            final TextView plot = (TextView) layout.findViewById(R.id.plot);
            plot.setText(movie.plot.equals("null") ? "" : movie.plot);
            fab = (FloatingActionButton) layout.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dataSource.isMovieExists(movie.id)) {
                        fab.setImageResource(R.drawable.ic_favorite_white_48dp);
                        dataSource.removeMovie(movie.id);
                        Toast.makeText(getActivity(), "Removed from Favorites.", Toast.LENGTH_LONG).show();
                    } else {
                        fab.setImageResource(R.drawable.ic_favorite_red_48dp);
                        dataSource.insertMovie(movie);
                        Toast.makeText(getActivity(), "Added " + movie.title + " To Favorites!", Toast.LENGTH_LONG).show();
                    }

                }
            });
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    View[] animateviews = {date, rating, plot};
                    for (View view : animateviews) {
                        view.setAlpha(0f);
                        view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                        view.setTranslationY(75);
                        view.animate().alpha(1.0f).setInterpolator(new DecelerateInterpolator()).translationY(0).start();
                        view.setVisibility(View.VISIBLE);
                    }
                    Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_anim);
                    fab.startAnimation(anim);
                    fab.setVisibility(View.VISIBLE);
                }
            }, 250);

        }
        layout.findViewById(R.id.reviews).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReviewActivity.class);
                intent.putExtra(ReviewActivity.KEY_ID, movie.id);
                startActivity(intent);
            }
        });
        dataSource = new FavoritesDataSource(getActivity());
        dataSource.open(false);
        if (dataSource.isMovieExists(movie.id)) {
            fab.setImageResource(R.drawable.ic_favorite_red_48dp);
        }
        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dataSource.close();
    }
}
