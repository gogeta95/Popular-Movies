package portfolio.saurabh.popularmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.squareup.picasso.Target;

import me.relex.circleindicator.CircleIndicator;
import portfolio.saurabh.popularmovies.database.FavoritesDataSource;
import portfolio.saurabh.popularmovies.util.MaterialColorMapUtils;

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
        final View layout = inflater.inflate(R.layout.movie_detail_fragment, container, false);
//        super.onCreate(savedInstanceState);
        dataSource = new FavoritesDataSource(getActivity());
        dataSource.open(false);
        Iconify.with(new FontAwesomeModule());
        movie = getArguments().getParcelable(KEY_MOVIE);
//        if (savedInstanceState == null) {
        final ImageView poster = (ImageView) layout.findViewById(R.id.poster);
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                MaterialColorMapUtils colorMapUtils = new MaterialColorMapUtils(getResources());
                int primaryColor = MaterialColorMapUtils.colorFromBitmap(bitmap);
                if (primaryColor != 0) {
                    MaterialColorMapUtils.MaterialPalette palette = colorMapUtils.calculatePrimaryAndSecondaryColor(primaryColor);
                    if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(palette.mPrimaryColor));
                    }
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().getWindow().setStatusBarColor(palette.mSecondaryColor);
                    }
                }
                poster.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(getActivity()).load(RecyclerAdapter.POSTER_BASE_URL + movie.posterurl).error(R.drawable.placeholder).into(target);
            pager = (ViewPager) layout.findViewById(R.id.pager);
            indicator = (CircleIndicator) layout.findViewById(R.id.indicator);
        Log.d("abc3", getActivity().toString());
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
                View[] animateviews = {layout.findViewById(R.id.date), layout.findViewById(R.id.rating), layout.findViewById(R.id.plot)};
                for (View view : animateviews) {
                    view.setAlpha(0f);
                    view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    view.setTranslationY(75);
                    view.animate().alpha(1.0f).setInterpolator(new DecelerateInterpolator()).translationY(0).start();
                    view.setVisibility(View.VISIBLE);
                }
                Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.fab_anim);
                fab.startAnimation(anim);
                fab.setVisibility(View.VISIBLE);
            }
        }, 250);
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
        if (dataSource != null)
            dataSource.close();
        Log.d("abc", "closed");
    }
}
