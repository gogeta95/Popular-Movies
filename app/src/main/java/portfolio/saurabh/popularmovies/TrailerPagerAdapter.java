package portfolio.saurabh.popularmovies;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class TrailerPagerAdapter extends FragmentPagerAdapter {
    List<String> thumbs;

    public TrailerPagerAdapter(FragmentManager fm,List<String> thumbs) {
        super(fm);
        this.thumbs=thumbs;
    }

    @Override
    public Fragment getItem(int position) {
        return TrailerFragment.getInstance(thumbs.get(position));
    }

    @Override
    public int getCount() {
        return thumbs.size();
    }
}
