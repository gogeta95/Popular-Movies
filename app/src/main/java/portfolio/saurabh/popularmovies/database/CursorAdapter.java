package portfolio.saurabh.popularmovies.database;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.Date;

import portfolio.saurabh.popularmovies.DetailsFragment;
import portfolio.saurabh.popularmovies.MainActivity;
import portfolio.saurabh.popularmovies.MovieData;
import portfolio.saurabh.popularmovies.MovieDetail;
import portfolio.saurabh.popularmovies.PosterViewHolder;
import portfolio.saurabh.popularmovies.R;
import portfolio.saurabh.popularmovies.RecyclerAdapter;

/**
 * Created by Saurabh on 1/2/2016.
 */
public class CursorAdapter extends CursorRecyclerViewAdapter<PosterViewHolder> {
    LayoutInflater inflater;
    Context context;

    public CursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public void onBindViewHolder(final PosterViewHolder holder, Cursor cursor) {
        int id = cursor.getInt(0);
        String title = cursor.getString(1);
        String poster = cursor.getString(2);
        String plot = cursor.getString(3);
        double rating = cursor.getDouble(4);
        Date release = new Date(cursor.getLong(5));
        String backdrop = cursor.getString(6);
        final MovieData movie = new MovieData(title, poster, plot, rating, release, backdrop, id);
        String poster_url = movie.posterurl;
        if (!(poster_url.isEmpty() || poster_url.equals("null"))) {
            Picasso.with(context).load(RecyclerAdapter.POSTER_BASE_URL + poster_url).error(R.drawable.placeholder).into(holder.poster);
        }
        if (!MainActivity.mIsDualPane) {
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MovieDetail.class);
                    intent.putExtra(MovieDetail.KEY_MOVIE, movie);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        AppBarLayout barLayout = (AppBarLayout) ((AppCompatActivity) context).findViewById(R.id.actionbar);
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((AppCompatActivity) context, Pair.create((View) holder.poster, "poster"), Pair.create((View) barLayout, "actionbar"));
                        context.startActivity(intent, options.toBundle());
                    } else
                        context.startActivity(intent);
                }
            });
        } else {
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail, DetailsFragment.getInstance(movie))
                            .commit();
                }
            });
        }

    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PosterViewHolder(inflater.inflate(R.layout.recycler_item, parent, false));
    }
}
