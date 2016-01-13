package portfolio.saurabh.popularmovies;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.List;


public class TrailerPagerAdapter extends FragmentPagerAdapter {
    public static final String TAG = TrailerFragment.class.getSimpleName();
    List<String> thumbs;

    public TrailerPagerAdapter(FragmentManager fm,List<String> thumbs) {
        super(fm);
        this.thumbs=thumbs;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, thumbs.get(position));
        return TrailerFragment.getInstance(thumbs.get(position));
    }

    @Override
    public int getCount() {
        return thumbs.size();
    }
}
