package com.crazyhitty.chdev.ks.munch.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crazyhitty.chdev.ks.munch.R;
import com.crazyhitty.chdev.ks.munch.feeds.FeedsPresenter;
import com.crazyhitty.chdev.ks.munch.feeds.IFeedsView;
import com.crazyhitty.chdev.ks.munch.models.FeedItem;
import com.crazyhitty.chdev.ks.munch.models.SettingsPreferences;
import com.crazyhitty.chdev.ks.munch.services.SyncArticlesIntentService;
import com.crazyhitty.chdev.ks.munch.ui.adapters.FeedsRecyclerViewAdapter;
import com.crazyhitty.chdev.ks.munch.utils.FadeAnimationUtil;
import com.crazyhitty.chdev.ks.munch.utils.NetworkConnectionUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kartik_ch on 11/27/2015.
 */
public class FeedsFragment extends Fragment implements IFeedsView, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.linear_layout_empty_feeds)
    LinearLayout linearLayoutEmptyFeeds;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_view_feeds)
    RecyclerView recyclerViewFeeds;

    private FeedsPresenter mFeedsPresenter;
    private FeedsRecyclerViewAdapter mFeedsRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String mSource;

    public void setSource(String mSource) {
        this.mSource = mSource;
    }

    public FeedsFragment setInstance(String source) {
        FeedsFragment feedsFragment = new FeedsFragment();
        feedsFragment.setSource(source);
        return feedsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mFeedsPresenter == null) {
            mFeedsPresenter = new FeedsPresenter(this, getActivity());
        }

        swipeRefreshLayout.setOnRefreshListener(this);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerViewFeeds.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewFeeds.setLayoutManager(mLayoutManager);

        //To stop the lag
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFeedsFromDb();
            }
        }, 500);
    }

    private void loadFeedsFromDb() {
        if (mSource.equals("all_sources")) {
            mFeedsPresenter.attemptFeedLoadingFromDb();
        } else {
            mFeedsPresenter.attemptFeedLoadingFromDbBySource(mSource);
        }
    }

    @Override
    public void onRefresh() {
        if (NetworkConnectionUtil.isNetworkAvailable(getActivity())) {
            if (mSource.equals("all_sources")) {
                mFeedsPresenter.attemptFeedLoading();
            } else {
                mFeedsPresenter.attemptFeedLoading(mSource);
            }
        } else {
            swipeRefreshLayout.setRefreshing(false);
            NetworkConnectionUtil.showNoNetworkDialog(getActivity());
        }
    }

    @Override
    public void feedsLoaded(List<FeedItem> feedItems) {
        swipeRefreshLayout.setRefreshing(false);
        if (feedItems != null) {
            if (feedItems.size() == 0) {
                new FadeAnimationUtil(getActivity()).fadeInAlpha(linearLayoutEmptyFeeds, 500);
            } else {
                linearLayoutEmptyFeeds.setVisibility(View.INVISIBLE);
            }

            if (recyclerViewFeeds.getVisibility() != View.VISIBLE) {
                new FadeAnimationUtil(getActivity()).fadeInAlpha(recyclerViewFeeds, 500);
            }
            mFeedsRecyclerViewAdapter = new FeedsRecyclerViewAdapter(getActivity(), feedItems);
            recyclerViewFeeds.setAdapter(mFeedsRecyclerViewAdapter);
        } else {
            // this will only run if feeds are cleared by the user
            new FadeAnimationUtil(getActivity()).fadeInAlpha(linearLayoutEmptyFeeds, 500);
            new FadeAnimationUtil(getActivity()).fadeOutAlpha(recyclerViewFeeds, 500);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFeedsRecyclerViewAdapter = new FeedsRecyclerViewAdapter(getActivity(), null);
                    recyclerViewFeeds.setAdapter(mFeedsRecyclerViewAdapter);
                }
            }, 500);
        }
    }

    @Override
    public void loadingFailed(String message) {
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getActivity(), "Feeds failed to load\nError: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_feeds, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sync) {
            Intent intent = new Intent(getActivity(), SyncArticlesIntentService.class);
            //intent.putExtra(SyncArticlesIntentService.EXTRA_ARTICLE_LINKS, )
            getActivity().startService(intent);
        }

        if (id == R.id.action_sort) {
            MaterialDialog sortDialog = new MaterialDialog.Builder(getActivity())
                    .title(R.string.sort_feeds)
                    .positiveText(R.string.sort)
                    .negativeText(R.string.cancel)
                    .items(R.array.sort_feeds_types)
                    .itemsCallbackSingleChoice(SettingsPreferences.getSortingMethodPosition(getActivity()), new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            SettingsPreferences.setSortingMethodPosition(getActivity(), which);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadFeedsFromDb();
                                }
                            }, 500);
                            return false;
                        }
                    }).build();
            sortDialog.show();
            return true;
        }

        if (id == R.id.action_refresh) {
            onRefresh();
            return true;
        }

        if (id == R.id.action_clear_feeds) {
            MaterialDialog confirmDeleteDialog = new MaterialDialog.Builder(getActivity())
                    .title(R.string.clear_feeds)
                    .content(R.string.clear_feeds_desc)
                    .iconRes(R.drawable.ic_delete_24dp)
                    .positiveText(R.string.clear)
                    .negativeText(R.string.cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                            mFeedsPresenter.deleteFeeds();
                        }
                    }).build();
            confirmDeleteDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
