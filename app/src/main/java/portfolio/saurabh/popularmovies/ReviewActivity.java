package portfolio.saurabh.popularmovies;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import portfolio.saurabh.popularmovies.retrofit.MovieService;
import portfolio.saurabh.popularmovies.retrofit.ReviewList;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ReviewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String KEY_ID = "ID";
    RecyclerView recyclerView;
    ProgressBar progressBar;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.progress_colors));
        refreshLayout.setOnRefreshListener(this);
        onRefresh();
    }

    @Override
    public void onRefresh() {
//        new GetReviewsTask(this).execute(getIntent().getIntExtra(KEY_ID,0));
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UriBuilder.BASE)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieService service = retrofit.create(MovieService.class);
        service.listReviews(String.valueOf(getIntent().getIntExtra(KEY_ID, 0)), getString(R.string.api_key))
                .asObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ReviewList>() {
                    @Override
                    public void call(ReviewList reviewList) {
                        if (reviewList != null)
                            recyclerView.setAdapter(new ReviewAdapter(ReviewActivity.this, reviewList.reviewList));
                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        refreshLayout.setRefreshing(false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
        /*listCall.enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {
                ReviewList reviewList = response.body();
                if (reviewList != null)
                    recyclerView.setAdapter(new ReviewAdapter(ReviewActivity.this, reviewList.reviewList));
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ReviewList> call, Throwable t) {
                t.printStackTrace();
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}
