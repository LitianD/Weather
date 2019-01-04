package com.app.zlt.perfectweather.contract;


import com.app.zlt.perfectweather.model.data.NewsBean;


public interface INewsView {
    void showNews(NewsBean newsBean);

    void showMoreNews(NewsBean newsBean);

    void hideDialog();
    void showDialog();
    void showErrorMsg(Throwable throwable);
}
