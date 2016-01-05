package portfolio.saurabh.popularmovies;


import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeClipBounds;
import android.transition.Explode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_MENU_ITEM = "MENU_ITEM";
    public static boolean mIsDualPane;
    private static int menuitem;

    public void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
            getWindow().setSharedElementExitTransition(new ChangeClipBounds());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupWindowAnimations();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View detailView = findViewById(R.id.movie_detail);
        mIsDualPane = detailView != null && detailView.getVisibility() == View.VISIBLE;
        if (savedInstanceState != null && savedInstanceState.getInt(KEY_MENU_ITEM) != 0)
            menuitem = savedInstanceState.getInt(KEY_MENU_ITEM);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        LeakCanary.install(getApplication());
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, ListFragment.getInstance(UriBuilder.MOST_POPULAR))
                    .commit();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.d("abc", metrics.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menuitem != 0) {
            menu.findItem(menuitem).setChecked(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.isChecked())
            return super.onOptionsItemSelected(item);
        else {
            item.setChecked(true);
            if (item.getItemId() == R.id.action_favorites || item.getItemId() == R.id.action_popluar || item.getItemId() == R.id.action_rated)
                menuitem = item.getItemId();
            switch (item.getItemId()) {
                case R.id.action_popluar:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, ListFragment.getInstance(UriBuilder.MOST_POPULAR))
                            .commit();
                    return true;
                case R.id.action_rated:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, ListFragment.getInstance(UriBuilder.HIGHEST_RATED))
                            .commit();
                    return true;
                case R.id.action_favorites:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, new FavoritesFragment())
                            .commit();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_MENU_ITEM, menuitem);
    }
}

