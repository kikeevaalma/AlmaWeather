package com.alma.weather.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import com.alma.weather.app.MWApp;
import com.alma.weather.di.module.AppModule;
import com.alma.weather.di.module.ContextModule;
import com.alma.weather.presentation.presenter.WeatherPresenter;

@Singleton
@Component(modules = {
        ContextModule.class,
        AppModule.class,
        AndroidInjectionModule.class
})
public interface AppComponent {
    Context getContext();

    @Component.Builder
    interface Builder {
        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();

        Builder contextModule(ContextModule contextModule);
    }
    
    void inject(MWApp app);
    void inject(WeatherPresenter weatherPresenter);
}
