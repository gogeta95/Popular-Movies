package portfolio.saurabh.popularmovies.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.relex.circleindicator.CircleIndicator;
import portfolio.saurabh.popularmovies.R;
import portfolio.saurabh.popularmovies.data.Movie;
import portfolio.saurabh.popularmovies.data.MovieService;
import portfolio.saurabh.popularmovies.data.Trailer;
import portfolio.saurabh.popularmovies.data.TrailerList;
import portfolio.saurabh.popularmovies.database.MyDatabaseHelper;
import portfolio.saurabh.popularmovies.ui.detail.trailer.TrailerPagerAdapter;
import portfolio.saurabh.popularmovies.ui.review.ReviewActivity;
import portfolio.saurabh.popularmovies.util.DateConvert;
import portfolio.saurabh.popularmovies.util.MaterialColorMapUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static portfolio.saurabh.popularmovies.util.UriBuilder.POSTER_BASE_URL;


public class DetailsFragment extends Fragment {
    public static final String TAG = "MovieDetail";
    public static final String KEY_MOVIE = "MOVIE";
    @Inject
    MyDatabaseHelper myDatabaseHelper;
    @Inject
    MovieService service;
    private Movie movie;
    private ViewPager pager;
    private CircleIndicator indicator;
    private FloatingActionButton fab;
    private ShareActionProvider shareActionProvider;
    private LiveData<List<Trailer>> trailerLiveData;
    private TrailerPagerAdapter pagerAdapter;

    public static DetailsFragment getInstance(Parcelable movie) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_MOVIE, movie);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.movie_detail_fragment, container, false);
//        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        setHasOptionsMenu(true);
        movie = getArguments().getParcelable(KEY_MOVIE);
//        if (savedInstanceState == null) {
        final ImageView poster = layout.findViewById(R.id.poster);
        final FragmentActivity mActivity = getActivity();
        Glide.with(getActivity()).load(POSTER_BASE_URL + movie.posterurl).error(Glide.with(getActivity()).load(R.drawable.placeholder)).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                MaterialColorMapUtils colorMapUtils = new MaterialColorMapUtils(getResources());
                int primaryColor = MaterialColorMapUtils.colorFromBitmap(((BitmapDrawable) resource).getBitmap());
                if (primaryColor != 0) {
                    MaterialColorMapUtils.MaterialPalette palette = colorMapUtils.calculatePrimaryAndSecondaryColor(primaryColor);
                    if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(palette.mPrimaryColor));
                    }
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().getWindow().setStatusBarColor(palette.mSecondaryColor);
                    }
                }

                poster.setImageDrawable(resource);
            }
        });
        pager = layout.findViewById(R.id.pager);
        indicator = layout.findViewById(R.id.indicator);


//          Log.d("abc3", getActivity().toString());
        final TextView date = layout.findViewById(R.id.date);
        date.setText("In theatres " + DateConvert.convert(movie.release_date));
        final IconTextView rating = layout.findViewById(R.id.rating);
        rating.setText("{fa-star} " + movie.user_rating + "/10");
        final TextView plot = layout.findViewById(R.id.plot);
        plot.setText(movie.plot.equals("null") ? "" : movie.plot);

        fab = layout.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Observable.fromCallable(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        myDatabaseHelper.movieModel().setFavorite(movie.id, !movie.favorite);
                        movie.favorite = !movie.favorite;
                        return movie.favorite;
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {

                                if (aBoolean) {
                                    fab.setImageResource(R.drawable.ic_favorite_red_48dp);
                                    Toast.makeText(getActivity(), "Added " + movie.title + " To Favorites!", Toast.LENGTH_LONG).show();

                                } else {
                                    fab.setImageResource(R.drawable.ic_favorite_white_48dp);
                                    Toast.makeText(getActivity(), "Removed from Favorites.", Toast.LENGTH_LONG).show();
                                }

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                            }
                        });

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
//                Log.d("abc", getActivity().toString());
                Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.fab_anim);
                fab.startAnimation(anim);
                fab.setVisibility(View.VISIBLE);
            }
        }, 250);

        layout.findViewById(R.id.reviews).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReviewActivity.class);
                intent.putExtra(ReviewActivity.KEY_ID, movie.id);
                startActivity(intent);
            }
        });

        if (movie.favorite) {
            fab.setImageResource(R.drawable.ic_favorite_red_48dp);
        }

        AndroidSupportInjection.inject(this);


        pagerAdapter = new TrailerPagerAdapter(getChildFragmentManager());
        pager.setAdapter(pagerAdapter);
        indicator.setViewPager(pager);

        return layout;
    }

    private void observeTrailers() {
        trailerLiveData = myDatabaseHelper.trailerModel().getTrailersForMovie(movie.id);

        trailerLiveData.observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(@Nullable List<Trailer> trailers) {

                if (trailers != null && !trailers.isEmpty()) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out " + movie.title + "! https://www.youtube.com/watch?v=" + trailers.get(0).getKey());
                    shareIntent.setType("text/plain");
                    shareActionProvider.setShareIntent(shareIntent);
                }

                pagerAdapter.setTrailers(trailers);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (trailerLiveData != null)
            trailerLiveData.removeObservers(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.share_menu, menu);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.action_share));
        //Must start after share Provider has been initialized.
        observeTrailers();

        Call<TrailerList> listCall = service.listTrailers(String.valueOf(movie.id), getString(R.string.api_key));
        listCall.enqueue(new Callback<TrailerList>() {
            @Override
            public void onResponse(Call<TrailerList> call, Response<TrailerList> response) {
                final TrailerList list = response.body();
                if (list != null && list.trailers != null && !list.trailers.isEmpty()) {
                    Completable.fromCallable(new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            for (Trailer trailer : list.trailers) {
                                trailer.movieId = movie.id;
                            }

                            myDatabaseHelper.trailerModel().insertAll(list.trailers);
                            return null;
                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .subscribe();
                }

            }

            @Override
            public void onFailure(Call<TrailerList> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
