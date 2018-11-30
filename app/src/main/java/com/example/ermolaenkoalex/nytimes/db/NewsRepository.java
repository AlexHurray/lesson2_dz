package com.example.ermolaenkoalex.nytimes.db;

import android.util.Log;

import com.example.ermolaenkoalex.nytimes.model.NewsItem;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsRepository {

    private static final String LOG_TAG = "NewsRepository";

    @Inject
    NewsDao newsDao;

    public Disposable saveData(final List<NewsItem> newsList) {
        return Completable.fromCallable((Callable<Void>) () -> {
            if (newsList.size() > 0) {
                newsDao.deleteAll();
                NewsItem[] news = newsList.toArray(new NewsItem[newsList.size()]);
                newsDao.insertAll(news);
            }

            return null;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(LOG_TAG, newsList.toString())
                        , throwable -> Log.e(LOG_TAG, throwable.toString()));
    }

    public Completable saveItem(final NewsItem newsItem) {
        return newsDao.insert(newsItem);
    }

    public Observable<List<NewsItem>> getDataObservable() {
        return newsDao.getAll();
    }

    public Observable<NewsItem> getItem(int id) {
        return newsDao.getNewsById(id);
    }

    public Completable deleteItem(int id) {
        return Completable.fromCallable((Callable<Void>) () -> {

            newsDao.delete(id);
            return null;
        });
    }
}
