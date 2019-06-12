package com.alma.weather.app;

import retrofit2.http.GET;
import retrofit2.http.Query;
import com.alma.weather.presentation.model.WeatherBase;
import rx.Observable;

public interface MWApi {
    @GET("/data/2.5/forecast")
    Observable<WeatherBase> getWeatherByCityId(
            @Query("id") Integer id,
            @Query("units") String units
    );

    @GET("/data/2.5/forecast")
    Observable<WeatherBase> getWeatherByCoord(
            @Query("lat") Double lat,
            @Query("lon") Double lon,
            @Query("units") String units
    );
}
