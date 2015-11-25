package portfolio.saurabh.popularmovies;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(pager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}

class PagerAdapter extends FragmentPagerAdapter {
    String[] Titles;

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        Titles = context.getResources().getStringArray(R.array.tab_labels);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ListFragment.getInstance(UriBuilder.MOST_POPULAR);
            case 1:
                return ListFragment.getInstance(UriBuilder.HIGHEST_RATED);
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return Titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }
}

