package com.example.ermolaenkoalex.nytimes.ui.newslist;

import android.util.Log;

import com.example.ermolaenkoalex.nytimes.mock.DataUtils;
import com.example.ermolaenkoalex.nytimes.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsListViewModel extends ViewModel {

    private final String LOG_TAG = "NYTimes_Log_NewsListVM";

    private Disposable disposable;
    private List<NewsItem> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refresher;

    public void bind(RecyclerView recyclerView, SwipeRefreshLayout refresher) {
        this.recyclerView = recyclerView;
        this.refresher = refresher;
    }

    public void unbind() {
        recyclerView = null;
        refresher = null;
    }

    public List<NewsItem> getNews() {
        return newsList;
    }

    public void loadData() {
        disposable = Observable.fromCallable(DataUtils::generateNews)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> refresher.setRefreshing(true))
                .doFinally(() -> refresher.setRefreshing(false))
                .subscribe(newsItems -> {
                    Log.d(LOG_TAG, "subscribe call in " + Thread.currentThread());

                    ((NewsRecyclerAdapter) recyclerView.getAdapter()).setData(newsItems);
                    newsList.clear();
                    newsList.addAll(newsItems);
                });
    }

    public boolean hasData() {
        return !newsList.isEmpty();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}
