package com.example.ermolaenkoalex.nytimes.ui.main.newslist;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.ermolaenkoalex.nytimes.model.NewsItem;

import java.util.List;

import androidx.annotation.NonNull;

public interface NewsListView extends MvpView {
    @StateStrategyType(value = SingleStateStrategy.class)
    void showRefresher(boolean show);

    @StateStrategyType(value = SingleStateStrategy.class)
    void showData(List<NewsItem> data);

    @StateStrategyType(value = SingleStateStrategy.class)
    void showError(int errorMessageId, boolean isToast);
}
