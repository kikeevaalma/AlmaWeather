package com.alma.weather.app;

import com.alma.weather.presentation.model.WeatherBase;
import rx.Observable;

public class MWService {

    private MWApi mApi;

    public MWService(MWApi api) {
        mApi = api;
    }

    public Observable<WeatherBase> getWeatherById(Integer id) {
        return mApi.getWeatherByCityId(id, "metric");
    }

    public Observable<WeatherBase> getWeatherByCoord(Double lat, Double lon) {
        return mApi.getWeatherByCoord(lat, lon, "metric");
    }
}
