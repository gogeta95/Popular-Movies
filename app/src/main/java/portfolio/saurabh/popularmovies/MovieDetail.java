package portfolio.saurabh.popularmovies;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.widget.IconTextView;
import com.squareup.picasso.Picasso;

import me.relex.circleindicator.CircleIndicator;

public class MovieDetail extends AppCompatActivity {
    public static final String TAG = "MovieDetail";
    public static final String KEY_MOVIE = "MOVIE";
    MovieData movie;
    ViewPager pager;
    CircleIndicator indicator;

    public void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Slide(Gravity.BOTTOM));
            getWindow().setReturnTransition(new Explode());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupWindowAnimations();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Iconify.with(new FontAwesomeModule());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, getIntent().getExtras().toString());
        if (getIntent() != null && getIntent().getParcelableExtra(KEY_MOVIE) != null) {
            movie = getIntent().getParcelableExtra(KEY_MOVIE);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(movie.title);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            ImageView poster = (ImageView) findViewById(R.id.poster);
            Picasso.with(this).load(RecyclerAdapter.POSTER_BASE_URL + movie.posterurl).error(R.drawable.placeholder).into(poster);
            pager= (ViewPager) findViewById(R.id.pager);
            indicator = (CircleIndicator) findViewById(R.id.indicator);
            new FetchTrailersTask(this).execute(movie.id);
            final TextView date = (TextView) findViewById(R.id.date);
            date.setText("In theatres " + DateConvert.convert(movie.release_date));
            final IconTextView rating = (IconTextView) findViewById(R.id.rating);
            rating.setText("{fa-star} " + movie.user_rating + "/10");
            final TextView plot = (TextView) findViewById(R.id.plot);
            plot.setText(movie.plot.equals("null") ? "" : movie.plot);
            final FloatingActionButton fab= (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fab.setImageResource(R.drawable.ic_favorite_red_48dp);
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
                    Animation anim= AnimationUtils.loadAnimation(MovieDetail.this,R.anim.fab_anim);
                    fab.startAnimation(anim);
                    fab.setVisibility(View.VISIBLE);
                }
            },250);

        } else
            finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
