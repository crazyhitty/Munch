package com.crazyhitty.chdev.ks.munch.article;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crazyhitty.chdev.ks.munch.R;
import com.crazyhitty.chdev.ks.munch.models.FeedItem;
import com.crazyhitty.chdev.ks.munch.utils.DatabaseUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Kartik_ch on 12/2/2015.
 */
public class ArticleInteractor implements IArticleInteractor {
    ArticleAsyncLoader mArticleAsyncLoader;
    private OnArticleLoadedListener mOnArticleLoadedListener;
    private MaterialDialog mMaterialDialog;

    public void loadArticleAsync(OnArticleLoadedListener onArticleLoadedListener, Context context, String url) {
        this.mOnArticleLoadedListener = onArticleLoadedListener;
        mArticleAsyncLoader = new ArticleAsyncLoader(url);
        mArticleAsyncLoader.execute();
        showLoadingDialog(context);
    }

    private void showLoadingDialog(Context context) {
        mMaterialDialog = new MaterialDialog.Builder(context)
                .title(R.string.loading)
                .content(R.string.please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .cancelable(false)
                .negativeText(R.string.cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        if (mArticleAsyncLoader != null) {
                            mArticleAsyncLoader.cancel(true);
                        }
                        mOnArticleLoadedListener.onFailure("User performed dismiss action");
                    }
                })
                .build();
        mMaterialDialog.show();

        mMaterialDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                mOnArticleLoadedListener.onFailure("User performed dismiss action");
            }
        });
    }

    public void articleLoaded(String articleBody) {
        mMaterialDialog.dismiss();
        mOnArticleLoadedListener.onSuccess("success", articleBody);
    }

    public void articleLoadingFailed(String message) {
        mMaterialDialog.dismiss();
        mOnArticleLoadedListener.onFailure(message);
    }

    //save article in db
    public void archiveArticleInDb(OnArticleArchivedListener onArticleArchivedListener, Context context, FeedItem feedItem, String article) {
        DatabaseUtil databaseUtil = new DatabaseUtil(context);
        try {
            databaseUtil.saveArticle(feedItem, article);
            onArticleArchivedListener.onArticleSaved("success");
        } catch (Exception e) {
            e.printStackTrace();
            onArticleArchivedListener.onArticleSavingFailed(e.getMessage());
        }
    }

    public void deleteArticleInDb(OnArticleRemoveListener onArticleRemoveListener, Context context, FeedItem feedItem) {
        try {
            DatabaseUtil databaseUtil = new DatabaseUtil(context);
            databaseUtil.deleteArticle(feedItem);
            databaseUtil.removeDescFromFeed(feedItem);
            onArticleRemoveListener.onArticleDeleted("deleted");
        } catch (Exception e) {
            e.printStackTrace();
            onArticleRemoveListener.onArticleDeletionFailed(e.getMessage());
        }
    }

    protected class ArticleAsyncLoader extends AsyncTask<String, Integer, String> {

        String mUrl, mErrorMsg;
        Elements mParagraphs;

        public ArticleAsyncLoader(String mUrl) {
            this.mUrl = mUrl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Document htmlDocument = Jsoup.connect(mUrl).get();
                mParagraphs = htmlDocument.select("p");
            } catch (IOException e) {
                e.printStackTrace();
                mErrorMsg = e.getMessage();
                return "failure";
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("success")) {
                String articleBody = getArticleBody(mParagraphs);
                articleLoaded(articleBody);
            } else if (s.equals("failure")) {
                articleLoadingFailed(mErrorMsg);
            }
            super.onPostExecute(s);
        }

        private String getArticleBody(Elements paragraphs) {
            String body = "";
            for (Element paragraph : paragraphs) {
                String para = paragraph.text().trim();
                if (!para.isEmpty()) {
                    body += para + "\n\n";
                }
            }
            if (body.length() != 0) {
                return body.substring(0, body.length() - 1);
            } else {
                return "No Content Available";
            }
        }
    }
}
