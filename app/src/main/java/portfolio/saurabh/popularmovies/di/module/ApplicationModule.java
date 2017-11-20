package portfolio.saurabh.popularmovies.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;

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

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import portfolio.saurabh.popularmovies.BuildConfig;
import portfolio.saurabh.popularmovies.R;
import portfolio.saurabh.popularmovies.UriBuilder;
import portfolio.saurabh.popularmovies.data.MovieService;
import portfolio.saurabh.popularmovies.database.MyDatabaseHelper;
import portfolio.saurabh.popularmovies.listfragment.ListFragmentContract;
import portfolio.saurabh.popularmovies.listfragment.ListFragmentPresenter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by saurabh on 20/11/17.
 */

@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return interceptor;
    }


    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient(HttpLoggingInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
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
    }

    @Provides
    @Singleton
    MovieService provideMovieService(OkHttpClient okHttpClient, Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UriBuilder.BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        return retrofit.create(MovieService.class);
    }

    @Provides
    @Singleton
    MyDatabaseHelper provideDbHelper() {
        return Room.databaseBuilder(application, MyDatabaseHelper.class, "Movie.db")
                .build();
    }

    @Provides
    @Singleton
    String provideApikey() {
        return application.getString(R.string.api_key);
    }

    @Provides
    ListFragmentContract.Presenter provideListPresenter(ListFragmentPresenter listFragmentPresenter) {
        return listFragmentPresenter;
    }
}
