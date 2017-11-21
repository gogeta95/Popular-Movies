package portfolio.saurabh.popularmovies.ui.review;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import portfolio.saurabh.popularmovies.R;
import portfolio.saurabh.popularmovies.data.ReviewData;
import portfolio.saurabh.popularmovies.di.ActivityContext;
import portfolio.saurabh.popularmovies.di.UIScope;

@UIScope
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<ReviewData> reviewDataList;

    @Inject
    ReviewAdapter(@ActivityContext Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    public void setReviewDataList(List<ReviewData> reviewDataList) {
        this.reviewDataList = reviewDataList;
        notifyDataSetChanged();
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewHolder(inflater.inflate(R.layout.review_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        ReviewData review = reviewDataList.get(position);
        holder.content.setText(review.review);
        holder.user.setText(review.user);
        int color = position % 2 == 0 ? context.getResources().getColor(R.color.blue) : context.getResources().getColor(R.color.green);
        holder.root.setCardBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return reviewDataList == null ? 0 : reviewDataList.size();
    }

    class ReviewHolder extends RecyclerView.ViewHolder {
        CardView root;
        TextView user;
        TextView content;

        ReviewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            user = itemView.findViewById(R.id.user);
            content = itemView.findViewById(R.id.content);
        }
    }
}

