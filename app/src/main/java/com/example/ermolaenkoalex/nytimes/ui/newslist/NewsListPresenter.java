package com.example.ermolaenkoalex.nytimes.ui.newslist;

import android.util.Log;

import com.example.ermolaenkoalex.nytimes.NetworkAPI.RestApi;
import com.example.ermolaenkoalex.nytimes.dto.ResultDTO;
import com.example.ermolaenkoalex.nytimes.dto.ResultsDTO;
import com.example.ermolaenkoalex.nytimes.model.NewsItem;
import com.example.ermolaenkoalex.nytimes.utils.NewsItemConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class NewsListPresenter extends ViewModel {

    private static final String LOG_TAG = "NewsListVM";

    @Nullable
    private Disposable disposable;

    @NonNull
    private List<NewsItem> newsList = new ArrayList<>();

    @Nullable
    private NewsListView newsListView;

    @NonNull
    private String currentSection = Section.Home.getSectionName();

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

    public void getNews(boolean forceReload, String section) {
        if (currentSection.equalsIgnoreCase(section)) {
            return;
        }

        currentSection = section;

        if (newsList.isEmpty() || forceReload) {
            loadData();
        } else {
            newsListView.setData(newsList);
        }
    }

    private void loadData() {
        dispose();

        if (newsListView != null) {
            newsListView.showState(ResponseState.Loading);
        }

        disposable = RestApi.getInstance()
                .news()
                .getNews(currentSection)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::checkResponseAndShowState, this::handleError);
    }

    @Override
    protected void onCleared() {
        dispose();
        super.onCleared();
    }

    private void showState(ResponseState state) {
        if (newsListView != null) {
            newsListView.showState(state);
        }
    }

    private void handleError(Throwable throwable) {
        Log.d(LOG_TAG, "handleError");

        if (throwable instanceof IOException) {
            showState(ResponseState.NetworkError);
            return;
        }
        showState(ResponseState.ServerError);
    }

    private void checkResponseAndShowState(@NonNull Response<ResultsDTO> response) {
        if (!response.isSuccessful()) {
            showState(ResponseState.ServerError);
            return;
        }

        final ResultsDTO body = response.body();
        if (body == null) {
            showState(ResponseState.HasNoData);
            return;
        }

        final List<ResultDTO> results = body.getResults();
        if (results == null) {
            showState(ResponseState.HasNoData);
            return;
        }

        if (results.isEmpty()) {
            showState(ResponseState.HasNoData);
            return;
        }

        newsList.clear();
        for (ResultDTO resultDTO : results) {
            newsList.add(NewsItemConverter.resultDTO2NewsItem(resultDTO));
        }

        if (newsListView != null) {
            newsListView.setData(newsList);
        }

        showState(ResponseState.HasData);
    }

    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }
}
