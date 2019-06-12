package com.alma.weather.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import com.alma.weather.app.MWApp;
import com.alma.weather.app.MWService;
import com.alma.weather.presentation.view.WeatherView;
import com.alma.weather.util.Utils;
import rx.Subscription;

@InjectViewState
public class WeatherPresenter extends BasePresenter<WeatherView> {
    @Inject
    public MWService mAwService;

    public WeatherPresenter() {
        MWApp.getAppComponent().inject(this);
    }

    public void loadWeatherByCityId(Integer id) {
        getViewState().startLoading();

        Subscription subscription = mAwService.getWeatherById(id)
                .compose(Utils.applySchedulers())
                .subscribe(weather -> {
                    getViewState().finishLoading();
                    getViewState().successLoading(weather);
                }, exception -> {
                    exception.printStackTrace();
                    getViewState().finishLoading();
                    getViewState().failedLoading(exception.getMessage());
                });
        unsubscribeOnDestroy(subscription);
    }

    public void loadWeatherByCoord(Double lat, Double lon) {
        getViewState().startLoading();

        Subscription subscription = mAwService.getWeatherByCoord(lat, lon)
                .compose(Utils.applySchedulers())
                .subscribe(weather -> {
                    getViewState().finishLoading();
                    getViewState().successLoading(weather);
                }, exception -> {
                    exception.printStackTrace();
                    getViewState().finishLoading();
                    getViewState().failedLoading(exception.getMessage());
                });
        unsubscribeOnDestroy(subscription);
    }
}
