package com.app.abby.perfectweather.activity;
import android.Manifest;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.app.abby.perfectweather.R;
import com.app.abby.perfectweather.base.BaseActivity;
import com.app.abby.perfectweather.base.WeatherApplication;
import com.app.abby.perfectweather.model.api.WeatherBean;
import com.app.abby.perfectweather.presenter.DrawerPresenter;
import com.app.abby.perfectweather.presenter.HomePagePresenter;
import com.app.abby.perfectweather.sevices.WeatherService;
import com.app.abby.perfectweather.util.FileSizeUtil;
import com.app.abby.perfectweather.util.SharedPreferenceUtil;
import com.app.abby.perfectweather.util.Util;
import com.app.abby.perfectweather.view.fragment.BlankFragment;
import com.app.abby.perfectweather.view.fragment.BlankFragment2;
import com.app.abby.perfectweather.view.fragment.DrawerFragment;
import com.app.abby.perfectweather.view.fragment.DrawerFragment.OnDrawerItemClick;
import com.app.abby.perfectweather.view.fragment.HomePageFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Abby on 8/13/2017.
 */

public class MainActivity extends BaseActivity implements
        HomePageFragment.OnFragmentInteractionListener, OnDrawerItemClick{

    private Unbinder unbinder;

    @BindView(R.id.update_time)
    TextView update_time;

    @BindView(R.id.temp)
    TextView temp;

    @BindView(R.id.condition)
    TextView condition;

    @BindView(R.id.city)
    TextView city;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;

    private AMapLocationClientOption mOtion;
    private AMapLocationClient mClient;
    DrawerPresenter drawerpresenter;

    HomePagePresenter pagePresenter;
    HomePageFragment homePageFragment;
    private BlankFragment blankFragment;
    private BlankFragment2 blankFragment2;
    private int fragmentId;

    private String cityName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        this.fragmentId = R.id.navigation_home;

        unbinder = ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        refreshLayout.setOnRefreshListener(refreshlayout -> {
            pagePresenter.loadWeather(SharedPreferenceUtil.getInstance().getCity(), false);
            refreshlayout.finishRefresh();
        });

        homePageFragment = HomePageFragment.newInstance();//获得首页对象
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homePageFragment).commit();
        //Util.addFragmentToActivity(getSupportFragmentManager(), homePageFragment, R.id.fragment_container);
        DrawerFragment drawerFragment = DrawerFragment.newInstance();
        Util.addFragmentToActivity(getSupportFragmentManager(), drawerFragment, R.id.fragment_container_drawer);

        pagePresenter = new HomePagePresenter(homePageFragment);
        drawerpresenter = new DrawerPresenter(drawerFragment);

        getPermission();//获得定位权限
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homePageFragment).commit();
                    pagePresenter.loadWeather(cityName, false);
                    fragmentId = R.id.navigation_home;
                    return true;
                case R.id.navigation_dashboard:
                    MainActivity.this.blankFragment = BlankFragment.newInstance("", "");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, blankFragment).commit();
                    fragmentId = R.id.navigation_dashboard;
                    return true;
                case R.id.navigation_notifications:
                    MainActivity.this.blankFragment2 = BlankFragment2.newInstance("", "");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, blankFragment2).commit();
                    fragmentId = R.id.navigation_notifications;
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mClient != null)
            mClient.onDestroy();
        mClient = null;
        mOtion = null;
        pagePresenter.onunsubscribe();
        pagePresenter = null;
        unbinder.unbind();
    }


    @Override//更新信息函数
    public void updateHeader(WeatherBean weather) {
        update_time.setText("更新时间" + weather.getHeWeather5().get(0).getBasic().getUpdate().getLoc());
        city.setText(weather.getHeWeather5().get(0).getBasic().getCity());
        temp.setText(weather.getHeWeather5().get(0).getNow().getTmp() + "℃");
        condition.setText(weather.getHeWeather5().get(0).getNow().getCond().getTxt());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }


    @Override//选择城市触发的函数
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.setting) {

            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);

        } else if (id == R.id.about) {

        }

        return super.onOptionsItemSelected(item);
    }


    public void getLocation() {

        //AMap location config
        mClient = new AMapLocationClient(WeatherApplication.getAppContext());
        mOtion = new AMapLocationClientOption();
        mOtion.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mOtion.setInterval(60000 * 60 * 24);
        mClient.setLocationOption(mOtion);
        mClient.setLocationListener(aMapLocation -> {

            pagePresenter.loadWeather(aMapLocation.getCity(), true);
            SharedPreferenceUtil.getInstance().setCity(aMapLocation.getCity());

            if (SharedPreferenceUtil.getInstance().getNotificationModel() == Notification.FLAG_ONGOING_EVENT) {
                //start the service when location changed
                Intent intent = new Intent(this, WeatherService.class);
                startService(intent);
            } else {
                Intent intent = new Intent(this, WeatherService.class);
                stopService(intent);
            }

        });
        mClient.startLocation();
        Log.d("getlocaton", "called");
    }


    @Override  //点击城市触发的函数
    public void onDrawerItemClick(String city) {
        this.cityName = city;
        if(this.fragmentId == R.id.navigation_home)
            pagePresenter.loadWeather(city, true);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void getPermission() {

        //need to check permission above android 6.0
        RxPermissions rxPermissions = new RxPermissions(this);

        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        getLocation();
                    } else {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            Toast.makeText(WeatherApplication.getAppContext(), "未获得定位权限，加载默认城市", Toast.LENGTH_LONG).show();
                        }
                        pagePresenter.loadWeather("beijing", true);
                    }
                });

    }


}












