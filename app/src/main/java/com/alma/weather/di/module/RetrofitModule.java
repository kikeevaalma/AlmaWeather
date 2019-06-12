package com.alma.weather.di.module;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import com.alma.weather.app.MWApp;
import com.alma.weather.util.ConnectivityInterceptor;
import rx.schedulers.Schedulers;

@Module
public class RetrofitModule {

    public static final String API_BASE_URL = "http://api.openweathermap.org";

    private static Interceptor key = (chain) -> {
        Request request = chain.request();
        HttpUrl url = request.url().newBuilder().addQueryParameter("APPID","a04c4ac12bd18094c19855e6853f5c31").build();
        request = request.newBuilder().url(url).build();
        return chain.proceed(request);
    };

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Retrofit.Builder builder) {
        if (!httpClient.interceptors().contains(key)) {
            httpClient.addInterceptor(key);
            httpClient.addInterceptor(new ConnectivityInterceptor(MWApp.getAppComponent().getContext()));
            httpClient.addNetworkInterceptor(new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY));
            builder.client(httpClient.build());
        }
        return builder.baseUrl(API_BASE_URL).build();
    }

    @Provides
    @Singleton
    public Retrofit.Builder provideRetrofitBuilder(Converter.Factory converterFactory) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(converterFactory);
    }

    @Provides
    @Singleton
    public Converter.Factory provideConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("dd.MM.yyyy HH:mm:ss")
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setFieldNamingStrategy(new CustomFieldNamingPolicy())
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .setLenient()
                .create();
    }

    private static class CustomFieldNamingPolicy implements FieldNamingStrategy {
        @Override
        public String translateName(Field field) {
            String name = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES.translateName(field);
            name = name.substring(2, name.length()).toLowerCase();
            return name;
        }
    }
}

