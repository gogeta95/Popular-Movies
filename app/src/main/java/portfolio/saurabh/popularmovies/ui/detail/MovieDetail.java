package portfolio.saurabh.popularmovies.ui.detail;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.transition.Explode;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import portfolio.saurabh.popularmovies.R;
import portfolio.saurabh.popularmovies.data.Movie;

public class MovieDetail extends AppCompatActivity implements HasSupportFragmentInjector {
    public static final String TAG = "MovieDetail";
    public static final String KEY_MOVIE = "MOVIE";

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    public void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new AutoTransition());
            getWindow().setReturnTransition(new Explode());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupWindowAnimations();
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        setContentView(R.layout.activity_movie_detail);
        Iconify.with(new FontAwesomeModule());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, getIntent().getExtras().toString());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(((Movie) getIntent().getParcelableExtra(KEY_MOVIE)).title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detailFragment, DetailsFragment.getInstance(getIntent().getParcelableExtra(KEY_MOVIE)), "TAG")
                    .commit();
        }
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

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
