package com.crazyhitty.chdev.ks.munch.sources;

import android.content.Context;

import com.crazyhitty.chdev.ks.munch.models.SourceItem;

/**
 * Created by Kartik_ch on 11/7/2015.
 */
public interface ISourcePresenter {
    void addSource(SourceItem sourceItem);

    void getSources();

    void getSourceItems();

    void modifySources(Context context, SourceItem sourceItem);

    void deleteSource(SourceItem sourceItem);
}
