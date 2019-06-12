package com.alma.weather.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import com.alma.weather.presentation.model.WeatherBase;

public interface WeatherView extends MvpView {
    void startLoading();
    void finishLoading();
    void failedLoading(String message);
    @StateStrategyType(AddToEndSingleStrategy.class)
    void successLoading(WeatherBase weatherBase);
}
