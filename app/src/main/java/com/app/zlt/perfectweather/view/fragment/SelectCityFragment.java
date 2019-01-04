package com.app.zlt.perfectweather.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.app.zlt.perfectweather.R;
import com.app.zlt.perfectweather.activity.MainActivity;
import com.app.zlt.perfectweather.model.RecyclerDecor.GroupedDecoration;
import com.app.zlt.perfectweather.model.api.ApiClient;
import com.app.zlt.perfectweather.model.api.WeatherBean;
import com.app.zlt.perfectweather.model.comparator.PinyinComparator;
import com.app.zlt.perfectweather.model.data.City;
import com.app.zlt.perfectweather.model.data.CityBean;
import com.app.zlt.perfectweather.model.data.Province;
import com.app.zlt.perfectweather.model.database.DBManager;
import com.app.zlt.perfectweather.model.database.DrawerItemORM;
import com.app.zlt.perfectweather.model.database.OrmLite;
import com.app.zlt.perfectweather.model.database.WeatherDB;
import com.app.zlt.perfectweather.view.adapter.CityListAdapter;
import com.github.promeg.pinyinhelper.Pinyin;
import com.litesuits.orm.db.assit.QueryBuilder;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

public class SelectCityFragment extends Fragment{

    private Subscription subscription;
    private List<String> mData;
    private List<Province>mProvinces;
    private RecyclerView mCitiList;
    private List<CityBean> mCityBean;
    private CityListAdapter mCityAdapter;
    public static final int LEVEL_PROVINCE = 1;
    private int mCurrentLevel=LEVEL_PROVINCE;
    public static final int LEVEL_CITY = 2;

    private Province mSelectedProvence;

    public SelectCityFragment() {
        mData=new ArrayList<>();
        mCityBean=new ArrayList<>();
        mProvinces=new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_select_city, container, false);
        mCitiList = (RecyclerView) rootView.findViewById(R.id.citylist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        mCitiList.setLayoutManager(linearLayoutManager);
        mCitiList.addItemDecoration(new GroupedDecoration(rootView.getContext(),mCityBean));
        mCityAdapter=new CityListAdapter(mData);


        mCityAdapter.setOnClickListener(position -> {

            if(mCurrentLevel==LEVEL_PROVINCE){

                mSelectedProvence=mProvinces.get(position);
                queryCities(mSelectedProvence.ProSort);
                mCitiList.smoothScrollToPosition(0);
                mCurrentLevel=LEVEL_CITY;

            }else if(mCurrentLevel==LEVEL_CITY){
                if(OrmLite.getInstance().query(new QueryBuilder<>(DrawerItemORM.class).where("mCity=?",mData.get(position))).size()==0) {
                    subscription=ApiClient.getInstance().fetchWeather(mData.get(position))
                            .subscribe(new Subscriber<WeatherBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(WeatherBean weatherBean) {

                                    OrmLite.getInstance().save(new DrawerItemORM(mData.get(position),weatherBean.getHeWeather5().get(0).getNow().getTmp()+"℃",weatherBean.getHeWeather5().get(0).getNow().getCond().getCode()));
                                    Toast.makeText(getContext(),"已经成功添加"+mData.get(position),Toast.LENGTH_SHORT).show();

                                    getActivity().finish();
                                }
                            });
                }else {

                    Toast.makeText(getContext(),"您已经添加过"+mData.get(position)+"了，无需重复添加",Toast.LENGTH_SHORT).show();

                }


            }

        });

        mCitiList.setAdapter(mCityAdapter);
        queryProvinces();
        return rootView;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(subscription!=null&&!subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }

    public void updateCities(List<City> cities,List<CityBean> cityBeen){
        if(mCityBean!=null)
            mCityBean.clear();

        mCityBean.addAll(cityBeen);
        if(mData.size()!=0){
            mData.clear();
        }

        for(int i=0;i<cities.size();i++){
            mData.add(cities.get(i).CityName);
        }

        mCityAdapter.notifyDataSetChanged();

    }

    public void updateProvinces(List<Province> provinces,List<CityBean> cityBeen){

        if(mProvinces!=null)
            mProvinces.clear();
        mProvinces.addAll(provinces);


        if(mCityBean!=null){
            mCityBean.clear();
        }
        mCityBean.addAll(cityBeen);


        if(mData.size()!=0)
            mData.clear();
        for(int i=0;i<provinces.size();i++){
            mData.add(provinces.get(i).ProName);
        }

        mCityAdapter.notifyDataSetChanged();
    }

    public void queryCities(int  portnum){
        List<City>mCities = new ArrayList<City>();
        List<CityBean>mCitiybeans = new ArrayList<CityBean>();
        List<String>c = new ArrayList<>();
        mCities.addAll(WeatherDB.loadCities(new DBManager().getDatabase(),portnum));
        Collections.sort(mCities,new PinyinComparator());

        for(int i=0;i<mCities.size();i++){
            String s=Pinyin.toPinyin(mCities.get(i).CityName,"").substring(0,1).toUpperCase();
            mCitiybeans.add(new CityBean(s,mCities.get(i).CityName));
            c.add(mCities.get(i).CityName);
        }
        Intent i = new Intent(getContext(),MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("cities", (ArrayList<String>) c);
        i.putExtras(bundle);
        updateCities(mCities,mCitiybeans);
    }

    private void queryProvinces() {
        List<Province>mProvinces = new ArrayList<Province>();
        List<CityBean>mCitiybeans = new ArrayList<CityBean>();
        List<String>Provinces_name = new ArrayList<String>();

        if (mCitiybeans != null)
            mCitiybeans.clear();

        if(mProvinces!=null){
            mProvinces.clear();
        }
        mProvinces.addAll(WeatherDB.loadProvinces(new DBManager().getDatabase()));
        Collections.sort(mProvinces, new PinyinComparator());

        for (int i = 0; i < mProvinces.size(); i++) {
            String s = Pinyin.toPinyin(mProvinces.get(i).ProName, "").substring(0, 1).toUpperCase();
            mCitiybeans.add(new CityBean(s, mProvinces.get(i).ProName));
        }
        updateProvinces(mProvinces, mCitiybeans);
    }
}
