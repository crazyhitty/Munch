package com.crazyhitty.chdev.ks.munch.article;

/**
 * Created by Kartik_ch on 12/2/2015.
 */
public interface IArticleView {
    void onArticleLoaded(String article);

    void onArticleFailedToLoad(String message);

    void onArticleSaved(String message);

    void onArticleSavingFailed(String message);

    void onArticleRemoved(String message);

    void onArticleRemovalFailed(String message);
}
