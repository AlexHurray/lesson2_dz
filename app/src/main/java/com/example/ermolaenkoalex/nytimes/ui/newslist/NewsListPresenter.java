package com.example.ermolaenkoalex.nytimes.ui.newslist;

import android.util.Log;

import com.example.ermolaenkoalex.nytimes.NetworkAPI.RestApi;
import com.example.ermolaenkoalex.nytimes.R;
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

public class NewsListPresenter extends ViewModel {

    private static final String LOG_TAG = "NewsListVM";

    @Nullable
    private Disposable disposable;

    @NonNull
    private List<NewsItem> newsList = new ArrayList<>();

    @Nullable
    private NewsListView newsListView;

    @NonNull
    private Section currentSection = Section.HOME;

    public void bind(@NonNull NewsListView newsListView) {
        this.newsListView = newsListView;
    }

    public void unbind() {
        newsListView = null;
    }

    public void getNews(boolean forceReload) {
        getNews(forceReload, currentSection);
    }

    public void getNews(boolean forceReload, Section section) {
        currentSection = section;

        if (newsList.isEmpty() || forceReload) {
            loadData();
        } else {
            newsListView.setData(newsList);
        }
    }

    private void loadData() {
        dispose();

        showState(new ResponseState(true, !newsList.isEmpty()));

        disposable = RestApi.getInstance()
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

    private void showState(@NonNull ResponseState state) {
        if (newsListView != null) {
            newsListView.showState(state);
        }
    }

    private void showStateWithData(@NonNull ResponseState state, @NonNull List<NewsItem> data) {
        if (newsListView != null) {
            newsListView.showState(state);
            newsListView.setData(data);
        }
    }

    private void handleError(Throwable throwable) {
        Log.d(LOG_TAG, "handleError");

        ResponseState state = new ResponseState(false, !newsList.isEmpty());

        if (throwable instanceof IOException) {
            state.setErrorMessage(R.string.error_network);
        } else {
            state.setErrorMessage(R.string.error_request);
        }

        showState(state);
    }

    private void checkResponseAndShowState(@NonNull ResultsDTO response) {
        final List<ResultDTO> results = response.getResults();
        if (results == null || results.isEmpty()) {
            ResponseState state = new ResponseState(false, false);
            state.setErrorMessage(R.string.error_data_is_empty);
            showState(state);
            return;
        }

        newsList.clear();
        for (ResultDTO resultDTO : results) {
            newsList.add(NewsItemConverter.resultDTO2NewsItem(resultDTO));
        }

        showStateWithData(new ResponseState(false, true), newsList);
    }

    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }
}
