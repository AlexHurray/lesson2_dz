package com.example.ermolaenkoalex.nytimes.ui.newslist;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ermolaenkoalex.nytimes.common.BaseActivity;
import com.example.ermolaenkoalex.nytimes.ui.about.AboutActivity;
import com.example.ermolaenkoalex.nytimes.R;
import com.example.ermolaenkoalex.nytimes.ui.newsdetails.NewsDetailsActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

public class NewsListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.refresher)
    SwipeRefreshLayout refresher;

    private NewsListViewModel viewModel;

    private final NewsRecyclerAdapter.OnItemClickListener clickListener = newsItem
            -> NewsDetailsActivity.start(this, newsItem);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        viewModel = ViewModelProviders.of(this).get(NewsListViewModel.class);

        NewsRecyclerAdapter adapter = new NewsRecyclerAdapter(this, clickListener);
        recyclerView.setAdapter(adapter);

        int numCol = getResources().getInteger(R.integer.news_columns_count);

        recyclerView.setLayoutManager(numCol == 1
                ? new LinearLayoutManager(this)
                : new GridLayoutManager(this, numCol));

        recyclerView.addItemDecoration(new ItemDecorationNewsList(
                getResources().getDimensionPixelSize(R.dimen.spacing_small), numCol));

        refresher.setOnRefreshListener(this);

        if (viewModel.hasData()) {
            adapter.setData(viewModel.getNews());
        } else {
            viewModel.bind(recyclerView, refresher);
            onRefresh();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.bind(recyclerView, refresher);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.unbind();
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
        viewModel.loadData();
    }
}
