package portfolio.saurabh.popularmovies.ui.review;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import portfolio.saurabh.popularmovies.R;
import portfolio.saurabh.popularmovies.data.MovieService;
import portfolio.saurabh.popularmovies.data.ReviewData;
import portfolio.saurabh.popularmovies.data.ReviewList;
import portfolio.saurabh.popularmovies.database.MyDatabaseHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String KEY_ID = "ID";
    @Inject
    ReviewAdapter reviewAdapter;
    @Inject
    MyDatabaseHelper myDatabaseHelper;
    @Inject
    MovieService service;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshLayout;
    private LiveData<List<ReviewData>> reviewLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AndroidInjection.inject(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);
        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.progress_colors));
        refreshLayout.setOnRefreshListener(this);
        recyclerView.setAdapter(reviewAdapter);

        observeReviews();

        onRefresh();
    }

    private void observeReviews() {

        int movieId = getIntent().getIntExtra(KEY_ID, 0);

        reviewLiveData = myDatabaseHelper.reviewModel().getReviewsForMovie(movieId);
        reviewLiveData
                .observe(this, new Observer<List<ReviewData>>() {
                    @Override
                    public void onChanged(@Nullable List<ReviewData> reviewData) {
                        reviewAdapter.setReviewDataList(reviewData);
                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    @Override
    public void onRefresh() {

        final int movieId = getIntent().getIntExtra(KEY_ID, 0);

        Call<ReviewList> listCall = service.listReviews(movieId, getString(R.string.api_key));
        listCall.enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {
                final ReviewList reviewList = response.body();
                if (reviewList != null && reviewList.reviewList != null) {
                    for (ReviewData reviewData : reviewList.reviewList) {
                        reviewData.movieId = movieId;
                    }

                    Completable.fromCallable(new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            myDatabaseHelper.reviewModel().insertAll(reviewList.reviewList);
                            return null;
                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .subscribe();
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ReviewList> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reviewLiveData.removeObservers(this);
    }
}
