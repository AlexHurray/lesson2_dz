package com.example.ermolaenkoalex.nytimes.ui.newslist;

import android.util.Log;

import com.example.ermolaenkoalex.nytimes.mock.DataUtils;
import com.example.ermolaenkoalex.nytimes.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsListPresenter extends ViewModel {

    private final String LOG_TAG = "NewsListVM";

    private Disposable disposable;
    private List<NewsItem> newsList = new ArrayList<>();
    private NewsListView newsListView;

    public void bind(NewsListView newsListView) {
        this.newsListView = newsListView;
    }

    public void unbind() {
        newsListView = null;
    }

    public void getNews(boolean force) {
        if (newsList.isEmpty() || force) {
            loadData();
        } else {
            newsListView.setData(newsList);
        }
    }

    public void loadData() {
        dispose();

        disposable = Observable.fromCallable(DataUtils::generateNews)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> newsListView.showLoading())
                .doFinally(newsListView::hideLoading)
                .subscribe(newsItems -> {
                    Log.d(LOG_TAG, "subscribe call in " + Thread.currentThread());

                    newsListView.setData(newsItems);
                    newsList.clear();
                    newsList.addAll(newsItems);
                });
    }

    @Override
    protected void onCleared() {
        dispose();
        super.onCleared();
    }

    private void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
