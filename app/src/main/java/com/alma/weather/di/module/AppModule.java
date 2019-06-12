package com.alma.weather.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import com.alma.weather.app.MWApi;
import com.alma.weather.app.MWService;

@Module(includes = {ApiModule.class})
public class AppModule {
    @Provides
    @Singleton
    MWService provideService(MWApi api) {
        return new MWService(api);
    }
}
