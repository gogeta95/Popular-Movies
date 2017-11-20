package portfolio.saurabh.popularmovies.ui.detail.trailer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import portfolio.saurabh.popularmovies.data.Trailer;


public class TrailerPagerAdapter extends FragmentPagerAdapter {
    public static final String TAG = TrailerFragment.class.getSimpleName();
    List<Trailer> trailers;

    public TrailerPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return TrailerFragment.getInstance(trailers.get(position).key);
    }

    @Override
    public int getCount() {
        return trailers == null ? 0 : trailers.size();
    }
}
