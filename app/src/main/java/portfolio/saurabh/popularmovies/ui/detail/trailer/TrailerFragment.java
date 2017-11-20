package portfolio.saurabh.popularmovies.ui.detail.trailer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import portfolio.saurabh.popularmovies.R;


/**
 * Created by Saurabh on 12/24/2015.
 */
public class TrailerFragment extends Fragment {
    public static final String KEY="LINK";
    private static final String TAG = TrailerFragment.class.getName();

    public static TrailerFragment getInstance(String key) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY, key);
        TrailerFragment fragment = new TrailerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.trailer_thumb, container, false);
        ImageView image = (ImageView) layout.findViewById(R.id.trailer_thumb);
        Glide.with(getActivity()).load("http://img.youtube.com/vi/"+getArguments().getString(KEY)+"/0.jpg").error(Glide.with(getActivity()).load(R.drawable.placeholder)).into(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/watch?v="+getArguments().getString(KEY)));
                startActivity(intent);
            }
        });
        return layout;
    }
}
