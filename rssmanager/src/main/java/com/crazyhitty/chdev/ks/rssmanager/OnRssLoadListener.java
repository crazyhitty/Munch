package com.crazyhitty.chdev.ks.rssmanager;

import java.util.List;

/**
 * Created by Kartik_ch on 11/15/2015.
 */
public interface OnRssLoadListener {
    void onSuccess(List<RssItem> rssItems);

    void onFailure(String message);
}
