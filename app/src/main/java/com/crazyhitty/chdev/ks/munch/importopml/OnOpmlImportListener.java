package com.crazyhitty.chdev.ks.munch.importopml;

import com.crazyhitty.chdev.ks.munch.models.SourceItem;

import java.util.List;

/**
 * Created by Kartik_ch on 1/9/2016.
 */
public interface OnOpmlImportListener {
    void onSuccess(List<SourceItem> sourceItems);

    void onFailure(String message);
}
