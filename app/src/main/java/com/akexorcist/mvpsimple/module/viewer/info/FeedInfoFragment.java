package com.akexorcist.mvpsimple.module.viewer.info;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akexorcist.mvpsimple.R;
import com.akexorcist.mvpsimple.network.model.PostList;

import org.parceler.Parcels;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedInfoFragment extends Fragment implements FeedInfoContractor.View {
    public static final String KEY_POST_ITEM = "key_post_item";

    private FeedInfoContractor.Presenter presenterFeedInfoContractor;
    private TextView tvTitle;

    public static FeedInfoFragment newInstance(PostList.Item postItem) {
        FeedInfoFragment fragment = new FeedInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_POST_ITEM, Parcels.wrap(postItem));
        fragment.setArguments(bundle);
        return fragment;
    }

    public FeedInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_viewer_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindView(view);
        setupView();
        createPresenter();

        if (savedInstanceState == null) {
            restoreArgument(getArguments());
            initialize();
        } else {
            restoreInstanceState(savedInstanceState);
            restoreView();
        }
    }

    private void createPresenter() {
        FeedInfoPresenter.createPresenter(this);
    }

    private void bindView(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
    }

    private void setupView() {

    }

    private void restoreView() {
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        presenterFeedInfoContractor.setPostItem((PostList.Item) Parcels.unwrap(savedInstanceState.getParcelable(KEY_POST_ITEM)));
    }

    private void restoreArgument(Bundle bundle) {
        presenterFeedInfoContractor.setPostItem((PostList.Item) Parcels.unwrap(bundle.getParcelable(KEY_POST_ITEM)));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_POST_ITEM, Parcels.wrap(presenterFeedInfoContractor.getPostItem()));
    }

    private void initialize() {

    }

    @Override
    public void setPresenter(FeedInfoContractor.Presenter presenter) {
        this.presenterFeedInfoContractor = presenter;
    }

    @Override
    public void setFeedItemTitle(String title) {
        tvTitle.setText(title);
    }
}
