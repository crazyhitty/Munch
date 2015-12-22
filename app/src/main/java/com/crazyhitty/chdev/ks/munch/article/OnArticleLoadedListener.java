package com.crazyhitty.chdev.ks.munch.article;

/**
 * Created by Kartik_ch on 12/2/2015.
 */
public interface OnArticleLoadedListener {
    void onSuccess(String message, String articleBody);

    void onFailure(String message);
}
