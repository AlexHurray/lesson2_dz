package com.example.ermolaenkoalex.nytimes.ui.newslist;

import com.example.ermolaenkoalex.nytimes.R;
import com.example.ermolaenkoalex.nytimes.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ResponseState {
    private boolean loading;
    @Nullable
    private Integer errorMessageId;
    @NonNull
    private List<NewsItem> data = new ArrayList<>();

    ResponseState(boolean loading) {
        this.loading = loading;
    }

    ResponseState(boolean loading, @NonNull List<NewsItem> data) {
        this.loading = loading;

        if (data.isEmpty()) {
            this.errorMessageId = R.string.error_data_is_empty;
        } else {
            this.data.addAll(data);
        }
    }

    public boolean isLoading() {
        return loading;
    }

    public boolean hasData() {
        return !data.isEmpty();
    }

    public List<NewsItem> getData() {
        return data;
    }

    public boolean hasError() {
        return errorMessageId != null;
    }

    public int getErrorMessage() {
        return errorMessageId;
    }

    public void setErrorMessage(@IdRes int errorMessage) {
        this.errorMessageId = errorMessage;
    }
}
