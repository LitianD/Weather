package com.app.zlt.perfectweather.model.newsModel;


import com.app.zlt.perfectweather.model.data.NewsBean;

/**
 * Created by Administrator on 2018/5/19.
 */

public interface INewsLoadListener {
    void success(NewsBean newsBean);
    void fail(Throwable throwable);

    void loadMoreSuccess(NewsBean newsBean);
}
