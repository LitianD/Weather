package com.app.zlt.perfectweather.presenter;



import com.app.zlt.perfectweather.contract.INewsPresenter;
import com.app.zlt.perfectweather.contract.INewsView;
import com.app.zlt.perfectweather.model.api.NewsAPI;
import com.app.zlt.perfectweather.model.data.NewsBean;
import com.app.zlt.perfectweather.model.newsModel.INewsLoadListener;
import com.app.zlt.perfectweather.model.newsModel.INewsModel;
import com.app.zlt.perfectweather.model.newsModel.NewsModel;
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
