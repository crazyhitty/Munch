package com.crazyhitty.chdev.ks.munch.sources;

import com.crazyhitty.chdev.ks.munch.models.SourceItem;

/**
 * Created by Kartik_ch on 11/8/2015.
 */
public interface ISourceInteractor {
    void addSourceToDb(OnSourceSavedListener onSourceSavedListener, SourceItem sourceItem);

    void getSourcesFromDb(OnSourcesLoadedListener onSourcesLoadedListener);

    void getSourceItemsFromDb(OnSourcesLoadedListener onSourcesLoadedListener);

    void editSourceItemInDb(OnSourcesModifyListener onSourcesModifyListener, SourceItem sourceItem, String sourceNameOld);

    void deleteSourceItemFromDb(OnSourcesModifyListener onSourcesModifyListener, SourceItem sourceItem);
}
