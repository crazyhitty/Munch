package com.crazyhitty.chdev.ks.munch.importopml;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crazyhitty.chdev.ks.munch.R;
import com.crazyhitty.chdev.ks.munch.models.SourceItem;
import com.crazyhitty.chdev.ks.munch.utils.DateUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kartik_ch on 1/8/2016.
 */
public class ImportOpmlInteractor implements IImportOpmlInteractor {
    private static String UNKNOWN = "Unknown";
    private OnOpmlImportListener mOnOpmlImportListener;
    private MaterialDialog mLoadingDialog;
    private OpmlAsyncParser mOpmlAsyncParser;

    private void initDialog(Context context) {
        mLoadingDialog = new MaterialDialog.Builder(context)
                .title(R.string.loading)
                .content(R.string.please_wait)
                .progressIndeterminateStyle(true)
                .progress(true, 0)
                .negativeText(R.string.cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        mOpmlAsyncParser.cancel(true);
                        mOnOpmlImportListener.onFailure("User performed dismiss action");
                    }
                })
                .cancelable(false)
                .build();
    }

    public void retrieveFeed(OnOpmlImportListener onOpmlImportListener, Context context, String url) {
        this.mOnOpmlImportListener = onOpmlImportListener;
        initDialog(context);
        mLoadingDialog.show();
        mOpmlAsyncParser = new OpmlAsyncParser(url);
        mOpmlAsyncParser.execute();
    }

    public void retrieveFeeds(OnOpmlImportListener onOpmlImportListener, Context context, File file) {
        this.mOnOpmlImportListener = onOpmlImportListener;

        try {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(file.toURI().toURL().toString());
            if (fileExtension.equals("opml")) {
                initDialog(context);
                mLoadingDialog.show();
                mOpmlAsyncParser = new OpmlAsyncParser(file);
                mOpmlAsyncParser.execute();
            } else {
                mOnOpmlImportListener.onFailure("Invalid file");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            mOnOpmlImportListener.onFailure(e.getMessage());
        }
    }

    private class OpmlAsyncParser extends AsyncTask<String, Integer, String> {

        private String mUrl;
        private File mFile;
        private Elements mOpmlItems;

        public OpmlAsyncParser(String mUrl) {
            this.mUrl = mUrl;
        }

        public OpmlAsyncParser(File mFile) {
            this.mFile = mFile;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            Document opmlDocument = null;
            try {
                if (mUrl != null) {
                    opmlDocument = Jsoup.connect(mUrl).parser(Parser.xmlParser()).get();
                } else {
                    opmlDocument = Jsoup.parse(mFile, "UTF-8");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }
            if (opmlDocument != null) {
                mOpmlItems = opmlDocument.select("outline");
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mLoadingDialog.dismiss();
            if (s.equals("success")) {
                Log.e("Opml Items", String.valueOf(mOpmlItems.size()));
                List<SourceItem> sourceItems = new ArrayList<>();
                for (Element opmlItem : mOpmlItems) {
                    String title = opmlItem.attr("text");
                    String url = null;
                    if (!opmlItem.attr("xmlUrl").isEmpty()) {
                        url = opmlItem.attr("xmlUrl");
                    } else if (opmlItem.attr("url").isEmpty()) {
                        url = opmlItem.attr("url");
                    }
                    if (url != null) {
                        SourceItem sourceItem = new SourceItem();
                        sourceItem.setSourceName(title);
                        sourceItem.setSourceUrl(url);
                        sourceItem.setSourceCategoryName(UNKNOWN);
                        sourceItem.setSourceDateAdded(new DateUtil().getCurrDate());
                        sourceItems.add(sourceItem);
                    }
                }
                mOnOpmlImportListener.onSuccess(sourceItems);
            } else {
                Log.e("Opml Parse Error", s);
                mOnOpmlImportListener.onFailure(s);
            }
        }
    }
}
