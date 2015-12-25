package portfolio.saurabh.popularmovies;

import android.os.AsyncTask;
import android.view.View;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Saurabh on 12/25/2015.
 */
public class GetReviewsTask extends AsyncTask<Integer,Void,Void> {
    ReviewActivity mReviewActivity;
    List<ReviewData> reviewDataList;
    public GetReviewsTask(ReviewActivity mReviewActivity){
        this.mReviewActivity=mReviewActivity;
    }
    @Override
    protected void onPreExecute() {
        mReviewActivity.recyclerView.setVisibility(View.INVISIBLE);
        mReviewActivity.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Integer... params) {
        try {
            UriBuilder uri = new UriBuilder(mReviewActivity, UriBuilder.BASE_URL_TRAILERS + params[0] + UriBuilder.REVEIWS);
            URL url= new URL(uri.toString());
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            StringBuilder sb = new StringBuilder();
            Scanner sc= new Scanner(connection.getInputStream());
            while (sc.hasNextLine()){
                sb.append(sc.nextLine());
            }
            connection.disconnect();
            reviewDataList=JSONParser.parseReviews(sb.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(reviewDataList!=null)
        mReviewActivity.recyclerView.setAdapter(new ReviewAdapter(mReviewActivity,reviewDataList));
        mReviewActivity.recyclerView.setVisibility(View.VISIBLE);
        mReviewActivity.progressBar.setVisibility(View.INVISIBLE);
        mReviewActivity.refreshLayout.setRefreshing(false);
    }
}
