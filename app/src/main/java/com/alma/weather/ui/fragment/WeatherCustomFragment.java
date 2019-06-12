package com.alma.weather.ui.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.alma.weather.R;
import com.alma.weather.presentation.model.Daily;
import com.alma.weather.presentation.model.WeatherBase;
import com.alma.weather.presentation.presenter.WeatherPresenter;
import com.alma.weather.presentation.view.WeatherView;
import com.alma.weather.util.CityID;

public class WeatherCustomFragment extends BaseFragment implements WeatherView {

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

    @BindView(R.id.city_id)
    EditText mCityIdView;
    @BindView(R.id.check)
    Button mCheckView;

    @BindView(R.id.daily)
    LinearLayout mDailyView;

    @BindView(R.id.dt)
    TextView mLastView;

    private View rootView;

    private int cId;

    public static WeatherCustomFragment newInstance() {
        return new WeatherCustomFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_custom, container, false);
        ButterKnife.bind(this, rootView);

        cId = CityID.SAN_FRANCISCO;
        mCityIdView.setText(String.valueOf(cId));
        mRefreshView.setOnRefreshListener(() -> mWeatherPresenter.loadWeatherByCityId(cId));
        mWeatherPresenter.loadWeatherByCityId(cId);

        mCheckView.setOnClickListener(v -> {
            if (!mCityIdView.getText().toString().isEmpty()) {
                cId = Integer.parseInt(mCityIdView.getText().toString());
                mWeatherPresenter.loadWeatherByCityId(cId);
            }
        });

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
        mErrorView.setAction("Повторить", v -> mWeatherPresenter.loadWeatherByCityId(cId));
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
        for (Daily d : weatherBase.getList()) {
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            TextView dt = new TextView(getActivity());
            TextView temp = new TextView(getActivity());
            dt.setText(new SimpleDateFormat("EEE, MMMM dd HH:mm", Locale.US)
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
}
