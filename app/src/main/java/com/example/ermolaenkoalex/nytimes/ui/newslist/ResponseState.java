package com.example.ermolaenkoalex.nytimes.ui.newslist;

import androidx.annotation.IdRes;

public class ResponseState {
    private boolean loading;
    private boolean hasData;
    private boolean hasError;
    private int errorMessageId;

    ResponseState(boolean loading, boolean hasData) {
        this.loading = loading;
        this.hasData = hasData;
        this.hasError = false;
    }

    public boolean isLoading() {
        return loading;
    }

    public boolean hasData() {
        return hasData;
    }

    public boolean hasError() {
        return hasError;
    }

    public int getErrorMessage() {
        return errorMessageId;
    }

    public void setErrorMessage(@IdRes int errorMessage) {
        this.errorMessageId = errorMessage;
        hasError = true;
    }
}
