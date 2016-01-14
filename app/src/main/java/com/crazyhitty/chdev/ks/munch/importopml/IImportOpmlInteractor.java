package com.crazyhitty.chdev.ks.munch.importopml;

import android.content.Context;

import java.io.File;

/**
 * Created by Kartik_ch on 1/8/2016.
 */
public interface IImportOpmlInteractor {
    void retrieveFeed(OnOpmlImportListener onOpmlImportListener, Context context, String url);

    void retrieveFeeds(OnOpmlImportListener onOpmlImportListener, Context context, File file);
}
