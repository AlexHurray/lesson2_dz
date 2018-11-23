package com.example.ermolaenkoalex.nytimes.ui.main.newsedit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ermolaenkoalex.nytimes.R;
import com.example.ermolaenkoalex.nytimes.common.BaseFragment;
import com.example.ermolaenkoalex.nytimes.model.NewsItem;

import javax.inject.Inject;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsEditFragment extends BaseFragment implements NewsEditView {
    private static final String KEY_ID = "KEY_ID";

    @BindView(R.id.et_title)
    EditText etTitle;

    @BindView(R.id.et_preview_text)
    EditText etPreviewText;

    @BindView(R.id.et_news_url)
    EditText etNewsUrl;

    @BindView(R.id.et_image_url)
    EditText etImageUrl;

    private int id;

    @NonNull
    @Inject
    NewsEditPresenter presenter;

    public static NewsEditFragment newInstance(int id) {
        NewsEditFragment newsEditFragment = new NewsEditFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ID, id);
        newsEditFragment.setArguments(bundle);

        return newsEditFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        setRetainInstance(true);
        return inflater.inflate(R.layout.fragment_news_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        id = getArguments().getInt(KEY_ID, 0);

        presenter = ViewModelProviders.of(this).get(NewsEditPresenter.class);

        setTitle(getString(R.string.title_edit_news), true);
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.bind(this);
        presenter.getNews(id);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unbind();
    }

    @Override
    public void setData(NewsItem data) {
        etTitle.setText(data.getTitle());
        etPreviewText.setText(data.getPreviewText());
        etNewsUrl.setText(data.getItemUrl());
        etImageUrl.setText(data.getImageUrl());
    }

    @Override
    public void updateData(NewsItem newsItem) {
        newsItem.setTitle(etTitle.getText().toString());
        newsItem.setPreviewText(etPreviewText.getText().toString());
        newsItem.setImageUrl(etImageUrl.getText().toString());
        newsItem.setItemUrl(etNewsUrl.getText().toString());
    }

    @Override
    public void close(@IdRes int errorMessage) {
        if (errorMessage != 0) {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        }

        goBack();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_news_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_apply:
                presenter.saveData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}