package com.app.zlt.perfectweather.model.newsModel;


import com.app.zlt.perfectweather.model.api.RetrofitHelper;
import com.app.zlt.perfectweather.model.data.NewsBean;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.app.zlt.perfectweather.model.api.NewsAPI.NEWS_HOST;

/**
 * Created by Administrator on 2018/5/19.
 */

public class NewsModel implements INewsModel {

    @Override
    public void loadNews(final String hostType, final int startPage, final String id,
                         final INewsLoadListener iNewsLoadListener) {
        RetrofitHelper retrofitHelper = new RetrofitHelper(NEWS_HOST);
        retrofitHelper.getNews(hostType, id, startPage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
               .subscribe(new Subscriber<NewsBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iNewsLoadListener.fail(e);
            }

            @Override
            public void onNext(NewsBean newsBean) {
                if (startPage != 0) {
                    iNewsLoadListener.loadMoreSuccess(newsBean);
                } else {
                    iNewsLoadListener.success(newsBean);
                }

            }
        });
    }
}
