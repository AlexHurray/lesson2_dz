package com.example.ermolaenkoalex.nytimes.ui.newslist;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ermolaenkoalex.nytimes.common.BaseActivity;
import com.example.ermolaenkoalex.nytimes.model.NewsItem;
import com.example.ermolaenkoalex.nytimes.ui.about.AboutActivity;
import com.example.ermolaenkoalex.nytimes.R;
import com.example.ermolaenkoalex.nytimes.ui.newsdetails.NewsDetailsActivity;
import com.google.android.material.chip.Chip;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

public class NewsListActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, NewsListView {

    @BindView(R.id.recycler_view)
    @NonNull
    RecyclerView recyclerView;

    @BindView(R.id.refresher)
    @NonNull
    SwipeRefreshLayout refresher;

    @BindView(R.id.tv_error)
    @NonNull
    TextView tvError;

    @BindView(R.id.tv_no_data)
    @NonNull
    TextView tvNoData;

    @BindView(R.id.ll_sections)
    @NonNull
    LinearLayout llSections;

    @NonNull
    private NewsListPresenter presenter;

    @NonNull
    private NewsRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        presenter = ViewModelProviders.of(this).get(NewsListPresenter.class);

        adapter = new NewsRecyclerAdapter(this, newsItem
                -> NewsDetailsActivity.start(this, newsItem.getItemUrl(), newsItem.getCategory()));
        recyclerView.setAdapter(adapter);

        int numCol = getResources().getInteger(R.integer.news_columns_count);

        recyclerView.setLayoutManager(numCol == 1
                ? new LinearLayoutManager(this)
                : new GridLayoutManager(this, numCol));

        recyclerView.addItemDecoration(new ItemDecorationNewsList(
                getResources().getDimensionPixelSize(R.dimen.spacing_small), numCol));

        refresher.setOnRefreshListener(this);

        addChips();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.bind(this);
        presenter.getNews(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unbind();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        presenter.getNews(true);
    }

    @Override
    public void setData(@NonNull List<NewsItem> data) {
        adapter.setData(data);
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void showState(@NonNull ResponseState state) {
        switch (state) {
            case HasData:
                refresher.setRefreshing(false);
                tvError.setVisibility(View.GONE);
                tvNoData.setVisibility(View.GONE);

                recyclerView.setVisibility(View.VISIBLE);
                break;

            case HasNoData:
                refresher.setRefreshing(false);
                recyclerView.setVisibility(View.GONE);
                tvError.setVisibility(View.GONE);

                tvNoData.setVisibility(View.VISIBLE);
                break;

            case NetworkError:
                refresher.setRefreshing(false);
                recyclerView.setVisibility(View.GONE);
                tvNoData.setVisibility(View.GONE);

                tvError.setText(getText(R.string.error_network));
                tvError.setVisibility(View.VISIBLE);
                break;

            case ServerError:
                refresher.setRefreshing(false);
                recyclerView.setVisibility(View.GONE);
                tvNoData.setVisibility(View.GONE);

                tvError.setText(getText(R.string.error_server));
                tvError.setVisibility(View.VISIBLE);
                break;

            case Loading:
                refresher.setRefreshing(true);
                break;

            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }
    }

    private void addChips() {

        for (Section section : Section.values()) {
            Chip chip = new Chip(this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            if (Build.VERSION.SDK_INT < 23) {
                chip.setTextAppearance(this, R.style.TextAppearance_AppCompat_Title_Inverse);
            } else {
                chip.setTextAppearance(R.style.TextAppearance_AppCompat_Title_Inverse);
            }

            int spacing = getResources().getDimensionPixelSize(R.dimen.spacing_standard);
            params.setMargins(spacing, spacing, spacing, spacing);
            params.setMarginStart(spacing);
            params.setMarginEnd(spacing);

            chip.setLayoutParams(params);
            chip.setChipBackgroundColorResource(R.color.colorPrimaryDark);
            chip.setText(getString(section.getSectionNameResId()));
            chip.setOnClickListener(view -> presenter.getNews(true, section.getSectionName()));

            llSections.addView(chip);
        }
    }
}
