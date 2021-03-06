package portfolio.saurabh.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class PosterViewHolder extends RecyclerView.ViewHolder {
    public ImageView poster;
    public FrameLayout root;

    public PosterViewHolder(View itemView) {
        super(itemView);
        poster = (ImageView) itemView.findViewById(R.id.poster);
        root = (FrameLayout) itemView.findViewById(R.id.root);
    }
}