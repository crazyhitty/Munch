package com.crazyhitty.chdev.ks.munch.article;

import android.content.Context;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crazyhitty.chdev.ks.munch.R;
import com.crazyhitty.chdev.ks.munch.models.FeedItem;

/**
 * Created by Kartik_ch on 12/2/2015.
 */
public class ArticlePresenter implements IArticlePresenter, OnArticleLoadedListener, OnArticleArchivedListener, OnArticleRemoveListener {
    private Context mContext;
    private IArticleView mView;
    private ArticleInteractor mArticleInteractor;

    public ArticlePresenter(Context context, IArticleView view) {
        this.mContext = context;
        this.mView = view;
        this.mArticleInteractor = new ArticleInteractor();
    }

    public void attemptArticleLoading(String url) {
        mArticleInteractor.loadArticleAsync(this, mContext, url);
    }

    @Override
    public void onSuccess(String message, String articleBody) {
        mView.onArticleLoaded(articleBody);
    }

    @Override
    public void onFailure(String message) {
        mView.onArticleFailedToLoad(message);
    }

    public void archiveArticle(FeedItem feedItem, String article) {
        mArticleInteractor.archiveArticleInDb(this, mContext, feedItem, article);
    }

    public void removeArticle(FeedItem feedItem) {
        final FeedItem feedItemFinal = feedItem;
        MaterialDialog removeArticleDialog = new MaterialDialog.Builder(mContext)
                .title(R.string.remove_archive)
                .content(R.string.remove_archive_content)
                .positiveText(R.string.remove)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        //remove this archive from db
                        mArticleInteractor.deleteArticleInDb(ArticlePresenter.this, mContext, feedItemFinal);
                    }
                }).build();
        removeArticleDialog.show();
    }

    @Override
    public void onArticleSaved(String message) {
        mView.onArticleSaved(message);
    }

    @Override
    public void onArticleSavingFailed(String message) {
        mView.onArticleSavingFailed(message);
    }

    @Override
    public void onArticleDeleted(String message) {
        mView.onArticleRemoved(message);
    }

    @Override
    public void onArticleDeletionFailed(String message) {
        mView.onArticleRemovalFailed(message);
    }
}
