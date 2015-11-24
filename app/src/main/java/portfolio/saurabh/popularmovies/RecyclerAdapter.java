package portfolio.saurabh.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<PosterViewHolder> {
    public static final String TAG = RecyclerAdapter.class.getName();
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185";
    LayoutInflater inflater;
    Context context;
    List<MovieData> movieDataList;

    public RecyclerAdapter(Context context, List<MovieData> movieDataList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.movieDataList = movieDataList;
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PosterViewHolder(inflater.inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        String poster_url = movieDataList.get(position).posterurl;
        if (!(poster_url.isEmpty() || poster_url.equals("null"))) {
            Picasso.with(context).load(IMAGE_BASE_URL + poster_url).into(holder.poster);
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return movieDataList == null ? 0 : movieDataList.size();
    }

}

class PosterViewHolder extends RecyclerView.ViewHolder {
    ImageView poster;
    FrameLayout root;

    public PosterViewHolder(View itemView) {
        super(itemView);
        poster = (ImageView) itemView.findViewById(R.id.poster);
        root = (FrameLayout) itemView.findViewById(R.id.root);
    }
}
