package com.crazyhitty.chdev.ks.rssmanager;

import org.jsoup.select.Elements;

/**
 * Created by Kartik_ch on 11/15/2015.
 */
public interface OnFeedLoadListener {
    void onSuccess(Elements elements);

    void onFailure(String message);
}
