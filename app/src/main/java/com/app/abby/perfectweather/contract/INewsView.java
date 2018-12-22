package com.app.abby.perfectweather.contract;


import com.app.abby.perfectweather.model.data.NewsBean;

/**
 * Created by Administrator on 2018/5/19.
 */

public interface INewsView {
    void showNews(NewsBean newsBean);

    void showMoreNews(NewsBean newsBean);

    void hideDialog();
    void showDialog();
    void showErrorMsg(Throwable throwable);
}
