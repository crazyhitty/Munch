package com.crazyhitty.chdev.ks.munch.feeds;

import com.crazyhitty.chdev.ks.munch.models.FeedItem;

import java.util.List;

/**
 * Created by Kartik_ch on 11/4/2015.
 */
public interface IFeedsView {
    void feedsLoaded(List<FeedItem> feedItems);

    void loadingFailed(String message);
}
