package com.alma.weather.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import com.alma.weather.app.MWApi;

@Module(includes = {RetrofitModule.class})
public class ApiModule {
    @Provides
    @Singleton
    public MWApi provideApi(Retrofit retrofit) {
        return retrofit.create(MWApi.class);
    }
}