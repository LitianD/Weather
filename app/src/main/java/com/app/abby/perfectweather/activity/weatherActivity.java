package com.app.abby.perfectweather.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.app.abby.perfectweather.R;
import com.app.abby.perfectweather.model.api.WeatherBean;
import com.app.abby.perfectweather.view.fragment.BlankFragment;
import com.app.abby.perfectweather.view.fragment.HomePageFragment;

import butterknife.BindView;

public class weatherActivity extends AppCompatActivity{

    private BlankFragment blankFragment;
    HomePageFragment homePageFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    weatherActivity.this.blankFragment = BlankFragment.newInstance("", "");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,blankFragment).commit();

                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //blankFragment = BlankFragment.newInstance("", "");
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,blankFragment).commit();
    }

}
