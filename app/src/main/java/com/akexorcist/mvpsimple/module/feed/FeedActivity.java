package com.akexorcist.mvpsimple.module.feed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.akexorcist.mvpsimple.R;
import com.akexorcist.mvpsimple.module.feed.adapter.FeedListAdapter;
import com.akexorcist.mvpsimple.module.viewer.ViewerActivity;
import com.akexorcist.mvpsimple.module.writer.BlogInfoActivity;
import com.akexorcist.mvpsimple.network.model.PostList;
import com.akexorcist.mvpsimple.utility.AnimationManager;

import org.parceler.Parcels;

import java.util.List;

public class FeedActivity extends AppCompatActivity implements FeedContractor.View, FeedListAdapter.OnItemClickListener, View.OnClickListener {
    private static final String KEY_POST_LIST = "key_post_list";
    private FeedContractor.Presenter presenterFeedContractor;
    private Button btnBlogInfo;
    private RecyclerView rvPostList;
    private FeedListAdapter feedListAdapter;
    private LinearLayout layoutLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.akexorcist.mvpsimple.R.layout.activity_feed);

        bindView();
        setupView();
        createPresenter();

        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
            restoreView();
        } else {
            restoreArgument(getIntent().getExtras());
            initialize();
        }
    }

    private void bindView() {
        btnBlogInfo = (Button) findViewById(R.id.btn_blog_info);
        rvPostList = (RecyclerView) findViewById(R.id.rv_post_list);
        layoutLoading = (LinearLayout) findViewById(R.id.layout_loading);
    }

    private void setupView() {
        btnBlogInfo.setOnClickListener(this);
        feedListAdapter = new FeedListAdapter();
        feedListAdapter.setOnItemClickListener(this);
        rvPostList.setAdapter(feedListAdapter);
        int columnCount = getResources().getInteger(R.integer.post_list_column_count);
        rvPostList.setLayoutManager(new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL));
    }

    private void createPresenter() {
        FeedPresenter.createPresenter(this);
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        presenterFeedContractor.setPostList((PostList) Parcels.unwrap(savedInstanceState.getParcelable(KEY_POST_LIST)), true);
    }

    private void restoreView() {

    }

    private void restoreArgument(Bundle bundle) {

    }

    private void initialize() {
        presenterFeedContractor.loadPostList();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_POST_LIST, Parcels.wrap(presenterFeedContractor.getPostList()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenterFeedContractor.start();
        feedListAdapter.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenterFeedContractor.stop();
        feedListAdapter.onStop();
    }

    @Override
    public void updatePostItemList(List<PostList.Item> postItemList) {
        feedListAdapter.setPostItemList(postItemList);
    }

    @Override
    public void showPostListLoadingFailure() {
        Toast.makeText(this, R.string.service_unavailable, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(boolean noAnimation) {
        applyViewFadeIn(layoutLoading, noAnimation);
        applyViewFadeOut(rvPostList, noAnimation);
    }

    @Override
    public void hideLoading(boolean noAnimation) {
        applyViewFadeOut(layoutLoading, noAnimation);
        applyViewFadeIn(rvPostList, noAnimation);
    }

    @Override
    public void goToFeedViewerActivity(PostList.Item postItem) {
        Intent intent = new Intent(this, ViewerActivity.class);
        intent.putExtra(ViewerActivity.KEY_POST_ITEM, Parcels.wrap(postItem));
        startActivity(intent);
    }

    @Override
    public void setPresenter(FeedContractor.Presenter presenter) {
        this.presenterFeedContractor = presenter;
    }

    private void applyViewFadeIn(View view, boolean noAnimation) {
        long duration = presenterFeedContractor.getAnimationDuration(noAnimation);
        AnimationManager.getInstance().applyViewFadeIn(view, duration);
    }

    private void applyViewFadeOut(final View view, boolean noAnimation) {
        long duration = presenterFeedContractor.getAnimationDuration(noAnimation);
        AnimationManager.getInstance().applyViewFadeOut(view, duration);
    }

    @Override
    public void onPostItemClick(RecyclerView.ViewHolder viewHolder, PostList.Item postItem, int i) {
        presenterFeedContractor.onItemClick(postItem, i);
    }

    @Override
    public void onClick(View view) {
        if (view == btnBlogInfo) {
            goToBlogInfo();
        }
    }

    private void goToBlogInfo() {
        Intent intent = new Intent(this, BlogInfoActivity.class);
        startActivity(intent);
    }
}
