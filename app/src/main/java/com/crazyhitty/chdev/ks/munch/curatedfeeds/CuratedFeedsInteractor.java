package com.crazyhitty.chdev.ks.munch.curatedfeeds;

import android.content.Context;
import android.os.Handler;

import com.crazyhitty.chdev.ks.munch.models.SourceItem;
import com.crazyhitty.chdev.ks.munch.utils.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Kartik_ch on 1/3/2016.
 */
public class CuratedFeedsInteractor implements ICuratedFeedsInteractor, Callback {
    private static String CURATED_FEEDS_API_URL = "https://www.dropbox.com/s/izgfc6y3n6dsnvo/curated_feeds.json?dl=1";
    private static String VERSION = "version";
    private static String RELEASE_DATE = "release_date";
    private static String LAST_MODIFIED = "last_modified";
    private static String CURATED_FEEDS = "curated_feeds";
    private static String SOURCE_NAME = "source_name";
    private static String SOURCE_URL = "source_url";
    private static String SOURCE_CATEGORY = "source_category";

    private OnCuratedFeedsRetrievedListener mOnCuratedFeedsRetrievedListener;
    private Context mContext;

    public void fetchCuratedFeedsFromServer(OnCuratedFeedsRetrievedListener onCuratedFeedsRetrievedListener, Context context) {
        this.mOnCuratedFeedsRetrievedListener = onCuratedFeedsRetrievedListener;
        this.mContext = context;

        Request request = new Request.Builder()
                .url(CURATED_FEEDS_API_URL)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();

        Call call = okHttpClient.newCall(request);

        call.enqueue(this);
    }

    @Override
    public void onFailure(Request request, IOException e) {
        final String errorMsg = e.getMessage();
        //run on the ui thread
        new Handler(mContext.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mOnCuratedFeedsRetrievedListener.onFailure(errorMsg);
            }
        });
    }

    @Override
    public void onResponse(Response response) throws IOException {
        if (response.isSuccessful()) {
            final String responseStr = response.body().string();
            //run on the ui thread
            new Handler(mContext.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    parseCuratedFeedsJson(responseStr);
                }
            });
        } else {
            final String responseMsg = response.message();
            //run on the ui thread
            new Handler(mContext.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mOnCuratedFeedsRetrievedListener.onFailure(responseMsg);
                }
            });
        }
    }

    private void parseCuratedFeedsJson(String curatedFeedsResponseStr) {
        try {
            JSONObject mainJsonObject = new JSONObject(curatedFeedsResponseStr);
            double version = ((Number) mainJsonObject.get(VERSION)).doubleValue();
            String releaseDate = mainJsonObject.getString(RELEASE_DATE);
            String lastModified = mainJsonObject.getString(LAST_MODIFIED);

            List<SourceItem> sourceItems = new ArrayList<>();

            JSONArray curatedFeedsJsonObject = mainJsonObject.getJSONArray(CURATED_FEEDS);
            for (int i = 0; i < curatedFeedsJsonObject.length(); i++) {
                String sourceName = curatedFeedsJsonObject.getJSONObject(i).getString(SOURCE_NAME);
                String sourceUrl = curatedFeedsJsonObject.getJSONObject(i).getString(SOURCE_URL);
                String sourceCategory = curatedFeedsJsonObject.getJSONObject(i).getString(SOURCE_CATEGORY);

                SourceItem sourceItem = new SourceItem();
                sourceItem.setSourceName(sourceName);
                sourceItem.setSourceUrl(sourceUrl);
                sourceItem.setSourceCategoryName(sourceCategory);
                sourceItem.setSourceDateAdded(new DateUtil().getCurrDate());

                sourceItems.add(sourceItem);
            }

            if (sourceItems != null) {
                mOnCuratedFeedsRetrievedListener.onSuccess(sourceItems);
            } else {
                mOnCuratedFeedsRetrievedListener.onFailure("Unable to fetch curated feeds");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            mOnCuratedFeedsRetrievedListener.onFailure(e.getMessage());
        }
    }
}
