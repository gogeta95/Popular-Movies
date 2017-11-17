package portfolio.saurabh.popularmovies;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import portfolio.saurabh.popularmovies.data.Movie;


public class RecyclerAdapter extends RecyclerView.Adapter<PosterViewHolder> {
    public static final String TAG = RecyclerAdapter.class.getName();
    public static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";
    public static final String BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w500";
    LayoutInflater inflater;
    Context context;
    List<Movie> movies;

    public RecyclerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        if (MainActivity.mIsDualPane) {
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail, DetailsFragment.getInstance(movies.get(0)))
                    .commit();
        }
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
        Log.d(TAG, "setMovies: ");
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PosterViewHolder(inflater.inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final PosterViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        String poster_url = movie.posterurl;
        if (poster_url != null && !(poster_url.isEmpty() || poster_url.equals("null"))) {
            Glide.with(context).load(POSTER_BASE_URL + poster_url).error(Glide.with(context).load(R.drawable.placeholder)).into(holder.poster);
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
                            .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top)
                            .replace(R.id.movie_detail, DetailsFragment.getInstance(movie))
                            .commit();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

}
