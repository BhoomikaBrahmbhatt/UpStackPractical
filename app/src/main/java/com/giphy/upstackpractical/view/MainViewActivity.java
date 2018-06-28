package com.giphy.upstackpractical.view;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.giphy.upstackpractical.R;
import com.giphy.upstackpractical.model.GiphyBean;
import com.giphy.upstackpractical.model.Vote;
import com.giphy.upstackpractical.model.Vote_;
import com.giphy.upstackpractical.presenter.ApiClient;
import com.giphy.upstackpractical.presenter.ApiInterface;
import com.giphy.upstackpractical.presenter.BaseApplication;
import com.giphy.upstackpractical.presenter.OnLoadMoreListener;
import com.giphy.upstackpractical.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewActivity extends AppCompatActivity {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progressBarCenter)
    ProgressBar progressBar;

    Utils utils;
    ApiInterface apiService;

    VideoAdapter mAdapter;
    List<GiphyBean.Datum> movies = new ArrayList<>();
    protected Handler handler;
    int limit = 25;
    int offset = 0;
    boolean searchCall = false;
    String searchQuery = null;
    private Box<Vote> notesBox;
    int pos = 0;
    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            GiphyBean.Datum dt = movies.get(pos);
            Vote votes = notesBox.query().equal(Vote_.idVideo, dt.getId()).build().findFirst();
            if (votes != null) {

                dt.setDownVote(votes.getDownVote());
                dt.setUpVote(votes.getUpVote());
            } else {
                dt.setUpVote("0");
                dt.setDownVote("0");
            }

            movies.set(pos, dt);

            mAdapter.notifyDataSetChanged();
            mAdapter.setLoaded();

        }
    };
    IntentFilter intentFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        ButterKnife.bind(this);

        initUI();

        initRecycler();

        callTrending();
    }

    private void initRecycler() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new VideoAdapter(this, movies, getApplicationContext(), recyclerView);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                offset++;
                if (searchCall)
                    callSearch();
                else
                    callTrending();
            }
        });
    }

    private void initUI() {
        intentFilter = new IntentFilter("fav.USER_ACTION");
        registerReceiver(myReceiver, intentFilter);

        utils = new Utils();

        handler = new Handler();
        apiService =
                ApiClient.getClient().create(ApiInterface.class);

        BoxStore boxStore = ((BaseApplication) getApplication()).getBoxStore();

        notesBox = boxStore.boxFor(Vote.class);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainViewActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainViewActivity.this.getComponentName()));
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                offset = 0;
                searchQuery = query;
                movies.clear();
                mAdapter.notifyDataSetChanged();
                callSearch();

                searchItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }


    public void setPos(int pos) {
        this.pos = pos;
    }


    private void callSearch() {
        showProgress();
        Call<GiphyBean> call = apiService.getSearchGiphyList(searchQuery, utils.API_KEY, String.valueOf(limit), String.valueOf(offset));
        call.enqueue(new Callback<GiphyBean>() {
            @Override
            public void onResponse(Call<GiphyBean> call, Response<GiphyBean> response) {
                searchCall = true;
                List<GiphyBean.Datum> data = response.body().getData();
                setData(data);
            }

            @Override
            public void onFailure(Call<GiphyBean> call, Throwable t) {
                // Log error here since request failed
                Log.e("Error", t.toString());
                hideProgress();
            }
        });
    }

    private void showProgress() {
        if (offset == 0)
            progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void callTrending() {
        showProgress();
        Call<GiphyBean> call = apiService.getTrendingGiphyList(utils.API_KEY, String.valueOf(limit), String.valueOf(offset));
        call.enqueue(new Callback<GiphyBean>() {
            @Override
            public void onResponse(Call<GiphyBean> call, Response<GiphyBean> response) {
                List<GiphyBean.Datum> data = response.body().getData();
                setData(data);
            }

            @Override
            public void onFailure(Call<GiphyBean> call, Throwable t) {
                // Log error here since request failed
                Log.e("Error", t.toString());
                hideProgress();
            }
        });
    }

    private void setData(List<GiphyBean.Datum> data) {

        for (int i = 0; i < data.size(); i++) {
            GiphyBean.Datum dt = data.get(i);
            Vote votes = notesBox.query().equal(Vote_.idVideo, dt.getId()).build().findFirst();
            if (votes != null) {
                dt.setDownVote(votes.getDownVote());
                dt.setUpVote(votes.getUpVote());
            } else {
                dt.setUpVote("0");
                dt.setDownVote("0");
            }
        }
        movies.addAll(data);

        mAdapter.notifyDataSetChanged();
        mAdapter.setLoaded();
        hideProgress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }
}
