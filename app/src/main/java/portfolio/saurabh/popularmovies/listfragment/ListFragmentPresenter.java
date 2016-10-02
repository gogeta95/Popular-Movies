package portfolio.saurabh.popularmovies.listfragment;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import portfolio.saurabh.popularmovies.R;
import portfolio.saurabh.popularmovies.UriBuilder;
import portfolio.saurabh.popularmovies.retrofit.MovieList;
import portfolio.saurabh.popularmovies.retrofit.MovieService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by saurabh on 3/10/16.
 */

public class ListFragmentPresenter implements ListFragmentContract.Presenter {
    public static final String TAG=ListFragmentPresenter.class.getSimpleName();
    private static MovieService service;
    private ListFragmentContract.View view;
    private String apiKey;
    private String title;
    private String errMsg;

    ListFragmentPresenter(@NonNull Context context) {
        apiKey = context.getString(R.string.api_key);
        errMsg = context.getString(R.string.connection_error);
        if (service == null) {
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                @Override
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    try {
                        return format.parse(json.getAsString());
                    } catch (ParseException e) {
                        return null;
                    }
                }
            }).create();
            HttpLoggingInterceptor loggingInterceptor= new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient= new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(UriBuilder.BASE)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
            service = retrofit.create(MovieService.class);
        }

    }


    @Override
    public void refresh() {
        if (view != null) {
            view.showProgressBar();
            Call<MovieList> listCall = service.listMovies(title, apiKey);
            listCall.enqueue(new Callback<MovieList>() {
                @Override
                public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                    if (response.isSuccessful() && response.body() != null&&view!=null) {
                        view.showList(response.body());
                    }
                    else {
                        if (view != null) {
                            view.showErrorInSnackbar(errMsg);
                        }
                    }
                }

                @Override
                public void onFailure(Call<MovieList> call, Throwable t) {
                    t.printStackTrace();
                    if (view != null) {
                        view.showErrorInSnackbar(errMsg);
                    }
                }
            });
        }
    }

    @Override
    public void setView(ListFragmentContract.View view) {
        this.view = view;
    }

    @Override
    public void setTitle(@NonNull String title) {
        this.title = title;
    }

}
