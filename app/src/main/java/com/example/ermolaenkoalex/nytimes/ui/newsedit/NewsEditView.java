package com.example.ermolaenkoalex.nytimes.ui.newsedit;

import com.example.ermolaenkoalex.nytimes.model.NewsItem;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

public interface NewsEditView {

    void setData(@NonNull NewsItem newsItem);

    void close(@IdRes int errorMessage);

    void showErrorMassage(@IdRes int errorMessage);

    void showProgress(boolean show);
}
