package portfolio.saurabh.popularmovies;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class TrailerPagerAdapter extends FragmentPagerAdapter {
    public static final String TAG = TrailerFragment.class.getSimpleName();
    List<Trailer> thumbs;

    public TrailerPagerAdapter(FragmentManager fm, List<Trailer> thumbs) {
        super(fm);
        this.thumbs = thumbs;
    }

    @Override
    public Fragment getItem(int position) {
//        Log.d(TAG, thumbs.get(position));
        return TrailerFragment.getInstance(thumbs.get(position).key);
    }

    @Override
    public int getCount() {
        return thumbs.size();
    }
}
