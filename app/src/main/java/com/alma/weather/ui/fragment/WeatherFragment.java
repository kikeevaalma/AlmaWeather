package com.alma.weather.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.alma.weather.R;
import com.alma.weather.presentation.model.Daily;
import com.alma.weather.presentation.model.WeatherBase;
import com.alma.weather.presentation.presenter.WeatherPresenter;
import com.alma.weather.presentation.view.WeatherView;

public class WeatherFragment extends BaseFragment implements WeatherView {

    private static final String CITY_ID = "city_id";

    @InjectPresenter
    WeatherPresenter mWeatherPresenter;

    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefreshView;
    @BindView(R.id.city_name)
    TextView mCityNameView;
    @BindView(R.id.city_weather)
    TextView mCityWeatherView;
    @BindView(R.id.current_temp)
    TextView mCurrentTempView;

    @BindView(R.id.daily)
    LinearLayout mDailyView;

    @BindView(R.id.dt)
    TextView mLastView;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private View rootView;

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    public static WeatherFragment newInstance(int cityId) {
        WeatherFragment weatherFragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putInt(CITY_ID, cityId);
        weatherFragment.setArguments(args);
        return weatherFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, rootView);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (getArguments() != null) {
            mRefreshView.setOnRefreshListener(() -> mWeatherPresenter.loadWeatherByCityId(getArguments().getInt(CITY_ID)));
            mWeatherPresenter.loadWeatherByCityId(getArguments().getInt(CITY_ID));
        } else {
            setupLocationListener();
            setupPermission();
            mRefreshView.setOnRefreshListener(this::setupPermission);
        }

        return rootView;
    }

    @Override
    public void startLoading() {
        mRefreshView.setRefreshing(true);
    }

    @Override
    public void finishLoading() {
        mRefreshView.setRefreshing(false);
    }

    @Override
    public void failedLoading(String message) {
        Snackbar mErrorView = Snackbar.make(rootView,
                message, Snackbar.LENGTH_SHORT);
        mErrorView.setAction("Повторить", v -> {
            if (getArguments() != null) {
                mWeatherPresenter.loadWeatherByCityId(getArguments().getInt(CITY_ID));
            } else {
                setupLocationListener();
                setupPermission();
            }
            mWeatherPresenter.loadWeatherByCityId(getArguments().getInt(CITY_ID));
        });
        mErrorView.show();
    }

    @Override
    public void successLoading(WeatherBase weatherBase) {
        mLastView.setText(String.format(getResources().getString(R.string.dt),
                new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
                        .format(new Date())));
        mCityNameView.setText(weatherBase.getCity().getName());

        mCityWeatherView.setText(new SimpleDateFormat("EEE, MMMM dd", Locale.US)
                .format(new Date()) + ", " +
                weatherBase.getList().get(0).getWeather().get(0).getDescription());
        mCurrentTempView.setText(String.format(getResources().getString(R.string.temp),
                weatherBase.getList().get(0).getMain().getTemp() + ""));

        mDailyView.removeAllViews();
        for (Daily d: weatherBase.getList()) {
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            TextView dt = new TextView(getActivity());
            TextView temp = new TextView(getActivity());
            dt.setText(new SimpleDateFormat("EEE, MMMM dd HH:mm")
                    .format(d.getDt() * 1000L));
            temp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            temp.setGravity(Gravity.END);
            temp.setText(String.format(getResources().getString(R.string.temp),
                    d.getMain().getTemp() + ""));
            layout.addView(dt);
            layout.addView(temp);
            mDailyView.addView(layout);
        }

    }

    private void setupPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET},
                        10);
            }
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private void setupLocationListener() {
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                locationManager.removeUpdates(this);
                Double lat = location.getLatitude();
                Double lon = location.getLongitude();
                mWeatherPresenter.loadWeatherByCoord(lat, lon);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
    }
}
