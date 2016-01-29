package com.crazyhitty.chdev.ks.munch.sources;

import android.content.Context;

import com.crazyhitty.chdev.ks.munch.models.SourceItem;
import com.crazyhitty.chdev.ks.munch.utils.DatabaseUtil;
import com.crazyhitty.chdev.ks.munch.utils.UrlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kartik_ch on 11/8/2015.
 */
public class SourceInteractor implements ISourceInteractor {
    private Context mContext;
    private OnSourceSavedListener onSourceSavedListener;

    public SourceInteractor(Context mContext) {
        this.mContext = mContext;
    }

    public void addSourceToDb(OnSourceSavedListener onSourceSavedListener, SourceItem sourceItem) {
        // main shit happens here
        this.onSourceSavedListener = onSourceSavedListener;

        String regexUrl = UrlUtil.REGEX_URL;

        if (sourceItem.getSourceName().isEmpty() && sourceItem.getSourceUrl().isEmpty() && sourceItem.getSourceCategoryName().isEmpty()) {
            onSourceSavedListener.onFailure("name_url_category_empty");
        } else if (sourceItem.getSourceName().isEmpty()) {
            onSourceSavedListener.onFailure("name_empty");
        } else if (sourceItem.getSourceUrl().isEmpty()) {
            onSourceSavedListener.onFailure("url_empty");
        } else if (sourceItem.getSourceCategoryName().isEmpty()) {
            onSourceSavedListener.onFailure("category_empty");
        } else if (!sourceItem.getSourceUrl().matches(regexUrl)) {
            onSourceSavedListener.onFailure("incorrect_url");
        } else {
            //Log.e("name", sourceItem.getSourceName());
            //Log.e("url", sourceItem.getSourceUrl());
            //Log.e("category", sourceItem.getSourceCategoryName());
            //Log.e("categoryImgId", String.valueOf(sourceItem.getSourceCategoryImgId()));
            //Log.e("date", sourceItem.getSourceDateAdded());

            DatabaseUtil databaseUtil = new DatabaseUtil(mContext);
            databaseUtil.saveSourceInDB(sourceItem);

            onSourceSavedListener.onSuccess("Source saved");
        }
    }

    public void getSourcesFromDb(OnSourcesLoadedListener onSourcesLoadedListener) {
        List<String> sourceNames = new ArrayList<>();
        try {
            //default value
            sourceNames.add("All Sources");

            List<SourceItem> sourceItems = new DatabaseUtil(mContext).getAllSources();

            for (SourceItem sourceItem : sourceItems) {
                sourceNames.add(sourceItem.getSourceName());
            }

            onSourcesLoadedListener.onSourceLoaded(sourceNames);
        } catch (Exception e) {
            e.printStackTrace();
            onSourcesLoadedListener.onSourceLoadingFailed(e.getMessage());
        }
    }


    public void getSourceItemsFromDb(OnSourcesLoadedListener onSourcesLoadedListener) {
        try {
            List<SourceItem> sourceItems = new DatabaseUtil(mContext).getAllSourceItems();
            onSourcesLoadedListener.onSourceItemsLoaded(sourceItems);
        } catch (Exception e) {
            e.printStackTrace();
            onSourcesLoadedListener.onSourceLoadingFailed(e.getMessage());
        }
    }

    @Override
    public void editSourceItemInDb(OnSourcesModifyListener onSourcesModifyListener, SourceItem sourceItem, String sourceNameOld) {
        try {
            DatabaseUtil databaseUtil = new DatabaseUtil(mContext);
            databaseUtil.modifySource(sourceItem, sourceNameOld);
            onSourcesModifyListener.onSourceModified(sourceItem, sourceNameOld);
        } catch (Exception e) {
            e.printStackTrace();
            onSourcesModifyListener.onSourceModifiedFailed(e.getMessage());
        }
    }

    @Override
    public void deleteSourceItemFromDb(OnSourcesModifyListener onSourcesModifyListener, SourceItem sourceItem) {
        try {
            DatabaseUtil databaseUtil = new DatabaseUtil(mContext);
            databaseUtil.deleteSourceItem(sourceItem);
            databaseUtil.deleteFeeds(sourceItem);
            onSourcesModifyListener.onSourceDeleted(sourceItem);
        } catch (Exception e) {
            e.printStackTrace();
            onSourcesModifyListener.onSourceDeletionFailed(e.getMessage());
        }
    }
}
