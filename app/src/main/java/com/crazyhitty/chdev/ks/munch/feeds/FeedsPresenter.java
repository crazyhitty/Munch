package com.crazyhitty.chdev.ks.munch.feeds;

import android.content.Context;

import com.crazyhitty.chdev.ks.munch.models.FeedItem;
import com.crazyhitty.chdev.ks.munch.models.SettingsPreferences;
import com.crazyhitty.chdev.ks.munch.models.SourceItem;
import com.crazyhitty.chdev.ks.munch.utils.DatabaseUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Kartik_ch on 11/4/2015.
 */
public class FeedsPresenter implements IFeedsPresenter, OnFeedsLoadedListener {

    private IFeedsView mIFeedsView;
    private FeedsLoaderInteractor mFeedsLoaderInteractor;
    private Context mContext;

    public FeedsPresenter(IFeedsView mIFeedsView, Context context) {
        this.mIFeedsView = mIFeedsView;
        this.mContext = context;
        this.mFeedsLoaderInteractor = new FeedsLoaderInteractor(context);
    }

    public void attemptFeedLoading() {
        try {
            List<SourceItem> sourceItems = new DatabaseUtil(mContext).getAllSources();
            mFeedsLoaderInteractor.loadFeedsAsync(this, sourceItems);
        } catch (Exception e) {
            e.printStackTrace();
            mIFeedsView.loadingFailed(e.getMessage());
        }
    }

    public void attemptFeedLoading(String source) {
        try {
            SourceItem sourceItem = new DatabaseUtil(mContext).getSourceItem(source);
            List<SourceItem> sourceItems = new ArrayList<>();
            sourceItems.add(sourceItem);
            mFeedsLoaderInteractor.loadFeedsAsync(this, sourceItems);
        } catch (Exception e) {
            e.printStackTrace();
            mIFeedsView.loadingFailed(e.getMessage());
        }
    }

    public void attemptFeedLoadingFromDb() {
        mFeedsLoaderInteractor.loadFeedsFromDb(this);
    }

    public void attemptFeedLoadingFromDbBySource(String source) {
        mFeedsLoaderInteractor.loadFeedsFromDbBySource(this, source);
    }


    public void deleteFeeds() {
        DatabaseUtil databaseUtil = new DatabaseUtil(mContext);
        try {
            databaseUtil.deleteAllFeeds();
            mIFeedsView.feedsLoaded(null);
        } catch (Exception e) {
            e.printStackTrace();
            mIFeedsView.loadingFailed(e.getMessage());
        }
    }

    @Override
    public void onSuccess(List<FeedItem> feedItems, boolean loadedNewFeeds) {
        //randomize the list
        Collections.shuffle(feedItems, new Random(System.nanoTime()));
        mIFeedsView.feedsLoaded(feedItems);
        if (loadedNewFeeds && SettingsPreferences.FEED_CACHE) {
            new DatabaseUtil(mContext).saveFeedsInDB(feedItems);
        }
    }

    @Override
    public void onFailure(String message) {
        mIFeedsView.loadingFailed(message);
    }
}
