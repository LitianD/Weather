package com.app.abby.perfectweather.activity;
import android.Manifest;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.app.abby.perfectweather.R;
import com.app.abby.perfectweather.base.WeatherApplication;
import com.app.abby.perfectweather.model.api.WeatherBean;
import com.app.abby.perfectweather.sevices.WeatherService;
import com.app.abby.perfectweather.util.SharedPreferenceUtil;
import com.app.abby.perfectweather.util.Util;
import com.app.abby.perfectweather.view.fragment.BlankFragment;
import com.app.abby.perfectweather.view.fragment.BlankFragment2;
import com.app.abby.perfectweather.view.fragment.BlankFragment3;
import com.app.abby.perfectweather.view.fragment.DrawerFragment;
import com.app.abby.perfectweather.view.fragment.DrawerFragment.OnDrawerItemClick;
import com.app.abby.perfectweather.view.fragment.HomePageFragment;
import com.yalantis.phoenix.PullToRefreshView;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements
        HomePageFragment.OnFragmentInteractionListener, OnDrawerItemClick{

    private Unbinder unbinder;

    TextView update_time;
    TextView temp;
    TextView condition;
    TextView city;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    PullToRefreshView refreshLayout;
    RelativeLayout header_layout;

    private AMapLocationClientOption mOtion;
    private AMapLocationClient mClient;

    HomePageFragment homePageFragment;
    private BlankFragment blankFragment;
    private BlankFragment2 blankFragment2;
    private BlankFragment3 blankFragment3;
    private int fragmentId;


    private String cityName;
    String login = "";
    String username="";
    String userid="";

    public void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            userid = bundle.getString("userid");
            login = bundle.getString("login_msg");
            username = bundle.getString("username");
        }

        this.update_time = (TextView)findViewById(R.id.update_time);
        this.temp = (TextView)findViewById(R.id.temp);
        this.condition = (TextView)findViewById(R.id.condition);
        this.city = (TextView)findViewById(R.id.city);
        this.drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        this.toolbar = (Toolbar)findViewById(R.id.toolbar);
        this.refreshLayout = (PullToRefreshView)findViewById(R.id.refresh_layout);
        this.header_layout = (RelativeLayout)findViewById(R.id.header);


        //初始化导航栏
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        this.fragmentId = R.id.navigation_home;

        unbinder = ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        //设置左侧菜单
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //设置刷新监听函数
        refreshLayout.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homePageFragment.loadWeather(SharedPreferenceUtil.getInstance().getCity(), false);
                refreshLayout.setRefreshing(false);
            }
        });
        /*refreshLayout.setOnRefreshListener(refreshlayout -> {
            homePageFragment.loadWeather(SharedPreferenceUtil.getInstance().getCity(), false);
            refreshlayout.finishRefresh();
        });*/
        //加载首页
        homePageFragment = HomePageFragment.newInstance();//获得首页对象
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homePageFragment).commit();
        //Util.addFragmentToActivity(getSupportFragmentManager(), homePageFragment, R.id.fragment_container);
        DrawerFragment drawerFragment = DrawerFragment.newInstance();
        Util.addFragmentToActivity(getSupportFragmentManager(), drawerFragment, R.id.fragment_container_drawer);

        getPermission();//获得定位权限
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        TextView tv_username;
        TextView tv_userid;
        View view ;
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    header_layout.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homePageFragment).commit();
                    homePageFragment.loadWeather(cityName, false);
                    fragmentId = R.id.navigation_home;
                    return true;
                case R.id.navigation_dashboard:
                    //header_layout.setVisibility(View.GONE);
                    MainActivity.this.blankFragment = BlankFragment.newInstance("", "");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, blankFragment).commit();
                    fragmentId = R.id.navigation_dashboard;
                    return true;
                case R.id.navigation_notifications:
                    if(login.equals("OK")){
                        header_layout.setVisibility(View.GONE);
                        MainActivity.this.blankFragment2 = BlankFragment2.newInstance(username, "id:" + userid);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, blankFragment2).commit();
                        fragmentId = R.id.navigation_notifications;
                        return true;
                    }else{
                        header_layout.setVisibility(View.GONE);
                        MainActivity.this.blankFragment3 = BlankFragment3.newInstance("", "");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, blankFragment3).commit();
                        fragmentId = R.id.navigation_notifications;
                        return true;
                    }
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

            homePageFragment.loadWeather(aMapLocation.getCity(), true);
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
            homePageFragment.loadWeather(city, true);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    //获取权限
    private void getPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        getLocation();
                    } else {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            Toast.makeText(WeatherApplication.getAppContext(), "未获得定位权限，加载默认城市", Toast.LENGTH_LONG).show();
                        }
                        homePageFragment.loadWeather("beijing", true);
                    }
                });
    }

}












