package com.crazyhitty.chdev.ks.munch.utils.comparator;

import com.crazyhitty.chdev.ks.munch.models.FeedItem;

import java.util.Comparator;

/**
 * Created by Kartik_ch on 1/6/2016.
 */
public class FeedTitleComparator implements Comparator<FeedItem> {
    @Override
    public int compare(FeedItem feedItem1, FeedItem feedItem2) {
        String title1 = feedItem1.getItemTitle().toLowerCase();
        String title2 = feedItem2.getItemTitle().toLowerCase();
        if (title1 == title2) {
            return 0;
        }
        if (title1 == null) {
            return -1;
        }
        if (title2 == null) {
            return 1;
        }
        return title1.compareTo(title2);
    }
}
