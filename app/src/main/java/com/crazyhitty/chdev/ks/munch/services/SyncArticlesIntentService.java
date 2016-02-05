package com.crazyhitty.chdev.ks.munch.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crazyhitty.chdev.ks.munch.R;
import com.crazyhitty.chdev.ks.munch.models.FeedItem;
import com.crazyhitty.chdev.ks.munch.receivers.SyncArticlesReceiver;
import com.crazyhitty.chdev.ks.munch.utils.DatabaseUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SyncArticlesIntentService extends IntentService {
    private static final int NOTIFICATION_ID = 1346;
    public static boolean STATUS = true;
    private MaterialDialog sSyncDialog;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;

    public SyncArticlesIntentService() {
        super("SyncArticlesIntentService");
    }

    private void initNotification() {
        //Create an Intent for the BroadcastReceiver
        Intent buttonIntent = new Intent(this, SyncArticlesReceiver.class);
        buttonIntent.putExtra("notificationId", NOTIFICATION_ID);

        //Create the PendingIntent
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(this, 0, buttonIntent, 0);

        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_sync_24dp);
        mBuilder.setContentTitle(getString(R.string.syncing_feeds));
        mBuilder.setContentText(getString(R.string.downloading_feeds));
        mBuilder.setProgress(100, 0, true);
        mBuilder.addAction(R.drawable.ic_close_24dp, getString(R.string.cancel), actionPendingIntent);
        mBuilder.setOngoing(true);

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void initCompleteNotification() {
        mBuilder = null;
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setOngoing(false);
        mBuilder.setSmallIcon(R.drawable.ic_done_all_24dp);
        mBuilder.setContentTitle(getString(R.string.feeds_synced));
        mBuilder.setContentText(getString(R.string.download_complete));
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            initNotification();
            final String[] articleLinks;
            try {
                STATUS = true;
                articleLinks = new DatabaseUtil(this).getFeedLinks();
                handleActionStartSync(articleLinks);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleActionStartSync(String[] articleLinks) {
        if (articleLinks != null) {
            parseWebArticle(articleLinks, 0);
        }
    }

    private void updateNotification(int percentageDone) {
        mBuilder.setProgress(100, percentageDone, false);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private int getPercentage(int position, int items) {
        return (position * 100) / items;
    }

    private void parseWebArticle(String[] links, int position) {
        if (position != links.length - 1 && STATUS) {
            try {
                Document htmlDocument = Jsoup.connect(links[position]).get();
                Elements paragraphs = htmlDocument.select("p");
                String body = "";
                for (Element paragraph : paragraphs) {
                    String para = paragraph.text().trim();
                    if (!para.isEmpty()) {
                        body += para + "\n\n";
                    }
                }
                try {
                    //FeedItem feedItem = new DatabaseUtil(this).getFeedByLink(links[position]);
                    FeedItem feedItem = new FeedItem();
                    feedItem.setItemLink(links[position]);
                    feedItem.setItemWebDescSync(body);
                    DatabaseUtil databaseUtil = new DatabaseUtil(this);
                    databaseUtil.saveFeedArticleDesc(feedItem);
                    databaseUtil.saveArticle(databaseUtil.getFeedByLink(links[position]), feedItem.getItemWebDescSync());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //updateNotification(getPercentage(position, links.length));
            parseWebArticle(links, position + 1);
        } else {
            //stop service here
            stopSelf();
            initCompleteNotification();
            //mNotificationManager.cancel(NOTIFICATION_ID);
        }
    }
}
