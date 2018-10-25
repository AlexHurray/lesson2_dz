package com.example.ermolaenkoalex.nytimes.ui.newslist;

import android.util.Log;

import com.example.ermolaenkoalex.nytimes.mock.DataUtils;
import com.example.ermolaenkoalex.nytimes.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsListPresenter extends ViewModel {

    private static final String LOG_TAG = "NewsListVM";

    private Disposable disposable;
    private List<NewsItem> newsList = new ArrayList<>();
    private NewsListView newsListView;

    public void bind(@NonNull NewsListView newsListView) {
        this.newsListView = newsListView;
    }

    public void unbind() {
        newsListView = null;
    }

    public void getNews(boolean forceReload) {
        if (newsList.isEmpty() || forceReload) {
            loadData();
        } else {
            newsListView.setData(newsList);
        }
    }

    private void loadData() {
        dispose();

        disposable = Observable.fromCallable(DataUtils::generateNews)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    if (newsListView != null) {
                        newsListView.showLoading();
                    }
                })
                .doFinally(() -> {
                    if (newsListView != null) {
                        newsListView.hideLoading();
                    }
                })
                .subscribe(newsItems -> {
                    Log.d(LOG_TAG, "subscribe call in " + Thread.currentThread());

                    if (newsListView != null) {
                        newsListView.setData(newsItems);
                    }
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
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
