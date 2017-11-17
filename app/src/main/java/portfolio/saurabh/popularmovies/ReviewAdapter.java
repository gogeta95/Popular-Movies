package portfolio.saurabh.popularmovies;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import portfolio.saurabh.popularmovies.data.ReviewData;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    Context context;
    LayoutInflater inflater;
    List<ReviewData> reviewDataList;

    public ReviewAdapter(Context context,List<ReviewData>  reviewDataList){
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.reviewDataList=reviewDataList;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewHolder(inflater.inflate(R.layout.review_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        ReviewData review= reviewDataList.get(position);
        holder.content.setText(review.review);
        holder.user.setText(review.user);
        int color=position%2==0?context.getResources().getColor(R.color.blue):context.getResources().getColor(R.color.green);
        holder.root.setCardBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return reviewDataList==null?0:reviewDataList.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder{
        CardView root;
        TextView user;
        TextView content;
        public ReviewHolder(View itemView) {
            super(itemView);
            root= (CardView) itemView.findViewById(R.id.root);
            user= (TextView) itemView.findViewById(R.id.user);
            content= (TextView) itemView.findViewById(R.id.content);
        }
    }
}

