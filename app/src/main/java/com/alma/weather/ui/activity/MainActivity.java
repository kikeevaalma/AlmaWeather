package com.alma.weather.ui.activity;

import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.ButterKnife;
import com.alma.weather.R;
import com.alma.weather.ui.fragment.WeatherCustomFragment;
import com.alma.weather.ui.fragment.WeatherFragment;
import com.alma.weather.util.CityID;

public class MainActivity extends AppCompatActivity {
String setContentView;
    private static int PAGE_COUNT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ViewPager mPager = findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
        addDotsIndicator(0);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addDotsIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return WeatherFragment.newInstance();
                case 1:
                    return WeatherFragment.newInstance(CityID.LONDON);
                case 2:
                    return WeatherFragment.newInstance(CityID.PARIS);
                case 3:
                    return WeatherFragment.newInstance(CityID.TOKYO);
                case 4:
                    return WeatherFragment.newInstance(CityID.NEW_YORK);
                case 5:
                    return WeatherCustomFragment.newInstance();
                default:
                    return WeatherCustomFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

    }

    private void addDotsIndicator(int pos) {
        LinearLayout mDotsLayout = findViewById(R.id.dots);
        TextView[] mDots = new TextView[PAGE_COUNT];
        mDotsLayout.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorPrimary));

            mDotsLayout.addView(mDots[i]);
        }
        mDots[pos].setTextColor(getResources().getColor(R.color.colorAccent));
    }
}
