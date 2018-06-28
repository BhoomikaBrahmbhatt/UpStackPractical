package com.giphy.upstackpractical.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.giphy.upstackpractical.R;
import com.giphy.upstackpractical.model.GiphyBean;
import com.giphy.upstackpractical.model.Vote;
import com.giphy.upstackpractical.model.Vote_;
import com.giphy.upstackpractical.presenter.BaseApplication;
import com.giphy.upstackpractical.presenter.OnLoadMoreListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.BoxStore;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {

    private List<GiphyBean.Datum> moviesList;
    Context context;

    /*private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
*/
    // before loading more.
    private int visibleThreshold = 25;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    MainViewActivity mActivity;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.fav)
        TextView fav;

        @BindView(R.id.image)
        ImageView img;

        public MyViewHolder(View view) {
            super(view);

            // binding view
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.card_view)
        public void onButtonClick() {
            int pos = this.getLayoutPosition();
            mActivity.setPos(pos);
            final String mp4 = moviesList.get(pos).getImages().getLooping().getMp4();
            String id = moviesList.get(pos).getId();
            String name = moviesList.get(pos).getTitle();
            Intent intent = new Intent(context, ExoPlayerActivity.class);
            intent.putExtra("url", mp4);  // pass your values and retrieve them in the other Activity using keyName
            intent.putExtra("id", id);
            intent.putExtra("title", name);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }
    }
/*

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progressBar1)
        ProgressBar pBar;

        public ProgressViewHolder(View v) {
            super(v);
            // binding view
            ButterKnife.bind(this, v);
        }
    }
*/

    public VideoAdapter(MainViewActivity mActivity, List<GiphyBean.Datum> moviesList, Context context, RecyclerView recyclerView) {
        this.moviesList = moviesList;
        this.mActivity = mActivity;
        this.context = context;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                .getLayoutManager();


        recyclerView
                .addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView,
                                           int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        totalItemCount = linearLayoutManager.getItemCount();
                        lastVisibleItem = linearLayoutManager
                                .findLastVisibleItemPosition();
                        if (!loading
                                && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                            // End has been reached
                            // Do something
                            // Toast.makeText(getApplicationContext(),"load more",Toast.LENGTH_SHORT).show();
                            if (onLoadMoreListener != null) {
                                onLoadMoreListener.onLoadMore();
                            }
                            loading = true;
                        }
                    }
                });
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_list_row, parent, false);

      /*  RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.video_list_row, parent, false);

            vh = new MyViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
      */   return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GiphyBean.Datum movie = moviesList.get(position);
        /*if (holder == null) {
            view = new ImageView(context);
        }
        */
        // if (holder instanceof MyViewHolder) {
        String type = "up: " + movie.getUpVote() + " \n" +
                "down: " + movie.getDownVote();

        String url = movie.getImages().getFixedHeightSmallStill().getUrl();
        Picasso.with(context).load(url).into(((MyViewHolder) holder).img);

         holder.title.setText(movie.getTitle());
         holder.fav.setText(type);
       /* } else {
            ((ProgressViewHolder) holder).pBar.setIndeterminate(true);
        }*/
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void setLoaded() {
        loading = false;
    }


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

}
