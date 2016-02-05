package com.crazyhitty.chdev.ks.munch.utils;

import android.content.Context;
import android.database.Cursor;

import com.crazyhitty.chdev.ks.mindb.DatabaseOperations;
import com.crazyhitty.chdev.ks.munch.R;
import com.crazyhitty.chdev.ks.munch.models.FeedItem;
import com.crazyhitty.chdev.ks.munch.models.SourceItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kartik_ch on 11/15/2015.
 */
public class DatabaseUtil {
    private Context mContext;

    public DatabaseUtil(Context mContext) {
        this.mContext = mContext;
    }

    public void saveSourceInDB(SourceItem sourceItem) {
        String[] columnNames = mContext.getResources().getStringArray(R.array.source_table_columns);

        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, "munch_db.sqlite");

        String[] values = {sourceItem.getSourceName()
                , sourceItem.getSourceUrl()
                , sourceItem.getSourceCategoryName()
                , String.valueOf(sourceItem.getSourceCategoryImgId())};

        try {
            databaseOperations.saveDataInDB("source_table", columnNames, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SourceItem> getAllSources() throws Exception {
        List<SourceItem> sourceItems = new ArrayList<>();

        String[] columnNames = mContext.getResources().getStringArray(R.array.source_table_columns);

        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, "munch_db.sqlite");
        Cursor cursor = databaseOperations.retrieveAllFromDB("source_table");
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    SourceItem sourceItem = new SourceItem();
                    sourceItem.setSourceName(cursor.getString(cursor.getColumnIndex(columnNames[0])));
                    sourceItem.setSourceUrl(cursor.getString(cursor.getColumnIndex(columnNames[1])));
                    sourceItem.setSourceCategoryName(cursor.getString(cursor.getColumnIndex(columnNames[2])));
                    sourceItem.setSourceCategoryImgId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(columnNames[3]))));
                    sourceItems.add(sourceItem);
                } while (cursor.moveToNext());
            }
        }

        return sourceItems;
    }

    public SourceItem getSourceItem(String sourceName) throws Exception {
        SourceItem sourceItem = new SourceItem();
        String[] columnNames = mContext.getResources().getStringArray(R.array.source_table_columns);
        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, "munch_db.sqlite");
        Cursor cursor = databaseOperations.retrieveFromDBCondition("source_table", columnNames, "WHERE source_name='" + sourceName + "'");
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    sourceItem.setSourceName(cursor.getString(cursor.getColumnIndex(columnNames[0])));
                    sourceItem.setSourceUrl(cursor.getString(cursor.getColumnIndex(columnNames[1])));
                    sourceItem.setSourceCategoryName(cursor.getString(cursor.getColumnIndex(columnNames[2])));
                    sourceItem.setSourceCategoryImgId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(columnNames[3]))));
                } while (cursor.moveToNext());
            }
        }
        return sourceItem;
    }

    public void saveFeedsInDB(List<FeedItem> feedItems) {
        String[] columnNames = mContext.getResources().getStringArray(R.array.feed_table_columns);

        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, "munch_db.sqlite");

        //let the user decide this
        /*try {
            //clear the old feeds
            databaseOperations.deleteAllFromDB("feed_table");
        }catch (Exception e){
            e.printStackTrace();
        }*/

        for (FeedItem feedItem : feedItems) {
            String[] values = {feedItem.getItemTitle()
                    , feedItem.getItemDesc()
                    , feedItem.getItemLink()
                    , feedItem.getItemImgUrl()
                    , feedItem.getItemCategory()
                    , feedItem.getItemSource()
                    , feedItem.getItemSourceUrl()
                    , feedItem.getItemPubDate()
                    , String.valueOf(feedItem.getItemCategoryImgId())
                    , ""};
            try {
                databaseOperations.saveDataInDB("feed_table", columnNames, values);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<FeedItem> getAllFeeds() throws Exception {
        List<FeedItem> feedItems = new ArrayList<>();

        String[] columnNames = mContext.getResources().getStringArray(R.array.feed_table_columns);

        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, "munch_db.sqlite");

        Cursor cursor = databaseOperations.retrieveAllFromDB("feed_table");

        /*switch (condition){
            case "no_order":
                cursor = databaseOperations.retrieveAllFromDB("feed_table");
                break;
            case "alphabetical_feeds":
                cursor = databaseOperations.retrieveAllFromDBCondition("feed_table", "ORDER BY item_name");
                break;
            case "alphabetical_sources":
                cursor = databaseOperations.retrieveAllFromDBCondition("feed_table", "ORDER BY item_source");
                break;
            case "pub_date":
                cursor = databaseOperations.retrieveAllFromDBCondition("feed_table", "ORDER BY item_pub_date");
                break;
        }*/

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    FeedItem feedItem = new FeedItem();
                    feedItem.setItemTitle(cursor.getString(cursor.getColumnIndex(columnNames[0])));
                    feedItem.setItemDesc(cursor.getString(cursor.getColumnIndex(columnNames[1])));
                    feedItem.setItemLink(cursor.getString(cursor.getColumnIndex(columnNames[2])));
                    feedItem.setItemImgUrl(cursor.getString(cursor.getColumnIndex(columnNames[3])));
                    feedItem.setItemCategory(cursor.getString(cursor.getColumnIndex(columnNames[4])));
                    feedItem.setItemSource(cursor.getString(cursor.getColumnIndex(columnNames[5])));
                    feedItem.setItemSourceUrl(cursor.getString(cursor.getColumnIndex(columnNames[6])));
                    feedItem.setItemPubDate(cursor.getString(cursor.getColumnIndex(columnNames[7])));
                    feedItem.setItemCategoryImgId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(columnNames[8]))));
                    feedItem.setItemWebDesc(cursor.getString(cursor.getColumnIndex(columnNames[9])));
                    feedItems.add(feedItem);
                } while (cursor.moveToNext());
            }
        }

        return feedItems;
    }

    public List<FeedItem> getFeedsBySource(String source) throws Exception {
        List<FeedItem> feedItems = new ArrayList<>();

        String[] columnNames = mContext.getResources().getStringArray(R.array.feed_table_columns);

        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, "munch_db.sqlite");

        String condition = "WHERE item_source='" + source + "'";

        Cursor cursor = databaseOperations.retrieveFromDBCondition("feed_table", columnNames, condition);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    FeedItem feedItem = new FeedItem();
                    feedItem.setItemTitle(cursor.getString(cursor.getColumnIndex(columnNames[0])));
                    feedItem.setItemDesc(cursor.getString(cursor.getColumnIndex(columnNames[1])));
                    feedItem.setItemLink(cursor.getString(cursor.getColumnIndex(columnNames[2])));
                    feedItem.setItemImgUrl(cursor.getString(cursor.getColumnIndex(columnNames[3])));
                    feedItem.setItemCategory(cursor.getString(cursor.getColumnIndex(columnNames[4])));
                    feedItem.setItemSource(cursor.getString(cursor.getColumnIndex(columnNames[5])));
                    feedItem.setItemSourceUrl(cursor.getString(cursor.getColumnIndex(columnNames[6])));
                    feedItem.setItemPubDate(cursor.getString(cursor.getColumnIndex(columnNames[7])));
                    feedItem.setItemCategoryImgId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(columnNames[8]))));
                    feedItem.setItemWebDesc(cursor.getString(cursor.getColumnIndex(columnNames[9])));
                    feedItems.add(feedItem);
                } while (cursor.moveToNext());
            }
        }

        return feedItems;
    }

    public FeedItem getFeedByLink(String link) throws Exception {
        FeedItem feedItem = new FeedItem();

        String[] columnNames = mContext.getResources().getStringArray(R.array.feed_table_columns);

        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, "munch_db.sqlite");

        String condition = "WHERE item_link='" + link + "'";

        Cursor cursor = databaseOperations.retrieveFromDBCondition("feed_table", columnNames, condition);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    feedItem.setItemTitle(cursor.getString(cursor.getColumnIndex(columnNames[0])));
                    feedItem.setItemDesc(cursor.getString(cursor.getColumnIndex(columnNames[1])));
                    feedItem.setItemLink(cursor.getString(cursor.getColumnIndex(columnNames[2])));
                    feedItem.setItemImgUrl(cursor.getString(cursor.getColumnIndex(columnNames[3])));
                    feedItem.setItemCategory(cursor.getString(cursor.getColumnIndex(columnNames[4])));
                    feedItem.setItemSource(cursor.getString(cursor.getColumnIndex(columnNames[5])));
                    feedItem.setItemSourceUrl(cursor.getString(cursor.getColumnIndex(columnNames[6])));
                    feedItem.setItemPubDate(cursor.getString(cursor.getColumnIndex(columnNames[7])));
                    feedItem.setItemCategoryImgId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(columnNames[8]))));
                    feedItem.setItemWebDesc(cursor.getString(cursor.getColumnIndex(columnNames[9])));
                    break;
                } while (cursor.moveToNext());
            }
        }

        return feedItem;
    }

    public void deleteAllFeeds() throws Exception {
        new DatabaseOperations(mContext, "munch_db.sqlite").deleteAllFromDB("feed_table");
    }

    public void saveArticle(FeedItem feedItem, String article) throws Exception {
        String[] columnNames = mContext.getResources().getStringArray(R.array.article_table_columns);

        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, "munch_db.sqlite");

        String[] values = {feedItem.getItemTitle()
                , feedItem.getItemDesc()
                , feedItem.getItemLink()
                , feedItem.getItemImgUrl()
                , feedItem.getItemCategory()
                , feedItem.getItemSource()
                , feedItem.getItemSourceUrl()
                , feedItem.getItemPubDate()
                , String.valueOf(feedItem.getItemCategoryImgId())
                , article};

        try {
            databaseOperations.saveDataInDB("article_table", columnNames, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<FeedItem> getSavedArticles() throws Exception {
        List<FeedItem> feedItems = new ArrayList<>();

        String[] columnNames = mContext.getResources().getStringArray(R.array.article_table_columns);

        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, "munch_db.sqlite");

        Cursor cursor = databaseOperations.retrieveFromDB("article_table", columnNames);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    FeedItem feedItem = new FeedItem();
                    feedItem.setItemTitle(cursor.getString(cursor.getColumnIndex(columnNames[0])));
                    feedItem.setItemDesc(cursor.getString(cursor.getColumnIndex(columnNames[1])));
                    feedItem.setItemLink(cursor.getString(cursor.getColumnIndex(columnNames[2])));
                    feedItem.setItemImgUrl(cursor.getString(cursor.getColumnIndex(columnNames[3])));
                    feedItem.setItemCategory(cursor.getString(cursor.getColumnIndex(columnNames[4])));
                    feedItem.setItemSource(cursor.getString(cursor.getColumnIndex(columnNames[5])));
                    feedItem.setItemSourceUrl(cursor.getString(cursor.getColumnIndex(columnNames[6])));
                    feedItem.setItemPubDate(cursor.getString(cursor.getColumnIndex(columnNames[7])));
                    feedItem.setItemCategoryImgId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(columnNames[8]))));
                    feedItem.setItemWebDesc(cursor.getString(cursor.getColumnIndex(columnNames[9])));
                    feedItems.add(feedItem);
                } while (cursor.moveToNext());
            }
        }

        return feedItems;
    }

    public List<SourceItem> getAllSourceItems() throws Exception {
        List<SourceItem> sourceItems = new ArrayList<>();
        String[] columnNames = mContext.getResources().getStringArray(R.array.source_table_columns);
        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, "munch_db.sqlite");
        Cursor cursor = databaseOperations.retrieveFromDB("source_table", columnNames);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    SourceItem sourceItem = new SourceItem();
                    sourceItem.setSourceName(cursor.getString(cursor.getColumnIndex(columnNames[0])));
                    sourceItem.setSourceUrl(cursor.getString(cursor.getColumnIndex(columnNames[1])));
                    sourceItem.setSourceCategoryName(cursor.getString(cursor.getColumnIndex(columnNames[2])));
                    sourceItem.setSourceCategoryImgId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(columnNames[3]))));
                    sourceItems.add(sourceItem);
                } while (cursor.moveToNext());
            }
        }
        return sourceItems;
    }

    public void modifySource(SourceItem sourceItem, String sourceNameOld) throws Exception {
        String[] columnNames = mContext.getResources().getStringArray(R.array.source_table_columns);
        String[] values = {sourceItem.getSourceName(), sourceItem.getSourceUrl(), sourceItem.getSourceCategoryName(), String.valueOf(sourceItem.getSourceCategoryImgId())};
        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, "munch_db.sqlite");
        databaseOperations.editDataInDB("source_table", columnNames, values, "WHERE source_name='" + sourceNameOld + "'");
    }

    public void deleteSourceItem(SourceItem sourceItem) throws Exception {
        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, "munch_db.sqlite");
        databaseOperations.deleteFromDB("source_table", "source_name", sourceItem.getSourceName());
    }

    public void deleteArticle(FeedItem feedItem) throws Exception {
        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, "munch_db.sqlite");
        databaseOperations.deleteFromDB("article_table", "article_name", feedItem.getItemTitle());
    }

    public void deleteFeeds(SourceItem sourceItem) throws Exception {
        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, "munch_db.sqlite");
        databaseOperations.deleteFromDB("feed_table", "item_source", sourceItem.getSourceName());
    }

    public void deleteAll() throws Exception {
        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, "munch_db.sqlite");
        databaseOperations.deleteAllFromDB("source_table");
        databaseOperations.deleteAllFromDB("article_table");
        databaseOperations.deleteAllFromDB("feed_table");
    }

    public void deleteSelectedFeeds(FeedItem feedItem) throws Exception {
        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, "munch_db.sqlite");
        databaseOperations.deleteFromDB("feed_table", "item_name", feedItem.getItemTitle());
    }

    public void saveFeedArticleDesc(FeedItem feedItem) throws Exception {
        /*String[] columnNames = mContext.getResources().getStringArray(R.array.feed_table_columns);
        String[] values = {feedItem.getItemTitle()
                , feedItem.getItemDesc()
                , feedItem.getItemLink()
                , feedItem.getItemImgUrl()
                , feedItem.getItemCategory()
                , feedItem.getItemSource()
                , feedItem.getItemSourceUrl()
                , feedItem.getItemPubDate()
                , String.valueOf(feedItem.getItemCategoryImgId())
                , null};*/
        String[] columnNames = new String[]{"item_desc_web"};
        String[] values = new String[]{feedItem.getItemWebDescSync()};
        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, "munch_db.sqlite");
        databaseOperations.editDataInDB("feed_table", columnNames, values, "WHERE item_link='" + feedItem.getItemLink() + "'");
    }

    public String[] getFeedLinks() throws Exception {
        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, "munch_db.sqlite");
        List<String> itemLinks = databaseOperations.retrieveFromDB("feed_table", "item_link");
        String[] links = new String[itemLinks.size()];
        for (int i = 0; i < itemLinks.size(); i++) {
            links[i] = itemLinks.get(i);
        }
        return links;
    }

    public void removeDescFromFeed(FeedItem feedItem) throws Exception {
        String[] columnNames = new String[]{"item_desc_web"};
        String[] values = new String[]{""};
        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, "munch_db.sqlite");
        databaseOperations.editDataInDB("feed_table", columnNames, values, "WHERE item_link='" + feedItem.getItemLink() + "'");
    }
}
