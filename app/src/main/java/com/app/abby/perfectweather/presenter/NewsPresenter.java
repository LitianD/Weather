package com.app.abby.perfectweather.presenter;



import com.app.abby.perfectweather.contract.INewsPresenter;
import com.app.abby.perfectweather.contract.INewsView;
import com.app.abby.perfectweather.model.api.NewsAPI;
import com.app.abby.perfectweather.model.data.NewsBean;
import com.app.abby.perfectweather.model.newsModel.INewsLoadListener;
import com.app.abby.perfectweather.model.newsModel.INewsModel;
import com.app.abby.perfectweather.model.newsModel.NewsModel;

/**
 * Created by Administrator on 2018/5/19.
 */

public class NewsPresenter implements INewsPresenter, INewsLoadListener{

    private INewsModel iNewsModel;
    private INewsView iNewsView;

    public NewsPresenter(INewsView iNewsView) {
        this.iNewsView = iNewsView;
        this.iNewsModel = new NewsModel();
    }

    @Override
    public void loadNews(int type, final int startPage) {
        if (startPage == 0) {
            iNewsView.showDialog();
        }
        iNewsModel.loadNews("headline", startPage, NewsAPI.HEADLINE_ID, this);
    }

    @Override
    public void success(NewsBean newsBean) {
        iNewsView.hideDialog();
        if (newsBean != null) {
            iNewsView.showNews(newsBean);
        }

    }

    @Override
    public void fail(Throwable throwable) {
        iNewsView.hideDialog();
        iNewsView.showErrorMsg(throwable);
    }

    @Override
    public void loadMoreSuccess(NewsBean newsBean) {
        iNewsView.hideDialog();
        iNewsView.showMoreNews(newsBean);
    }
}
