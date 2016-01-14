package com.crazyhitty.chdev.ks.munch.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crazyhitty.chdev.ks.munch.R;

/**
 * Created by Kartik_ch on 12/15/2015.
 */
public class SettingsPreferences {
    public static int SOURCE_NAME_SIZE;
    public static int SOURCE_CATEGORY_SIZE;
    public static int SOURCE_URL_SIZE;
    public static int FEED_PUBLISH_DATE_SIZE;
    public static int FEED_TITLE_SIZE;

    public static int ARTICLE_TITLE_SIZE;
    public static int ARTICLE_CATEGORY_SIZE;
    public static int ARTICLE_PUBLISH_DATE_SIZE;
    public static int ARTICLE_CONTENT_SIZE;

    public static boolean THEME;
    public static boolean FEED_CACHE;
    public static boolean IN_APP_BROWSER;
    public static boolean FEEDS_RECYCLER_VIEW_ANIMATION;
    public static boolean SOURCES_RECYCLER_VIEW_ANIMATION;
    public static boolean CIRCULAR_REVEAL;
    public static boolean CHANGE_LOG_DIALOG_SHOW;

    private static String SHARED_SP = "DEFAULT_SP";
    private static String NEW_INSTALL = "new_install";
    private static String FEEDS_SORT = "feeds_sort";
    private static String APP_VERSION = "app_version";

    public static void init(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        setFeedsFontSize(context, defaultSharedPreferences);
        setArticleFontSize(context, defaultSharedPreferences);
        setFeedsRecyclerViewAnimation(context, defaultSharedPreferences);
        setSourcesRecyclerViewAnimation(context, defaultSharedPreferences);
        setCircularReveal(context, defaultSharedPreferences);
        setTheme(context, defaultSharedPreferences);
        setFeedsCache(context, defaultSharedPreferences);
        setInAppBrowser(context, defaultSharedPreferences);
        isChangeDialogToBeShown(context);
    }

    private static void setFeedsFontSize(Context context, SharedPreferences defaultSharedPreferences) {
        String fontSizeKey = context.getResources().getString(R.string.perf_feeds_font_size_key);
        String fontSize = defaultSharedPreferences.getString(fontSizeKey, "Small");

        switch (fontSize) {
            case "Small":
                SOURCE_NAME_SIZE = 10;
                SOURCE_CATEGORY_SIZE = 10;
                SOURCE_URL_SIZE = 10;
                FEED_PUBLISH_DATE_SIZE = 10;
                FEED_TITLE_SIZE = 15;
                break;
            case "Medium":
                SOURCE_NAME_SIZE = 15;
                SOURCE_CATEGORY_SIZE = 15;
                SOURCE_URL_SIZE = 15;
                FEED_PUBLISH_DATE_SIZE = 15;
                FEED_TITLE_SIZE = 20;
                break;
            case "Large":
                SOURCE_NAME_SIZE = 20;
                SOURCE_CATEGORY_SIZE = 20;
                SOURCE_URL_SIZE = 20;
                FEED_PUBLISH_DATE_SIZE = 20;
                FEED_TITLE_SIZE = 25;
                break;
        }
    }

    private static void setArticleFontSize(Context context, SharedPreferences defaultSharedPreferences) {
        String fontSizeKey = context.getResources().getString(R.string.perf_article_font_size_key);
        String fontSize = defaultSharedPreferences.getString(fontSizeKey, "Medium");

        switch (fontSize) {
            case "Small":
                ARTICLE_TITLE_SIZE = 20;
                ARTICLE_CATEGORY_SIZE = 10;
                ARTICLE_PUBLISH_DATE_SIZE = 10;
                ARTICLE_CONTENT_SIZE = 15;
                break;
            case "Medium":
                ARTICLE_TITLE_SIZE = 25;
                ARTICLE_CATEGORY_SIZE = 15;
                ARTICLE_PUBLISH_DATE_SIZE = 15;
                ARTICLE_CONTENT_SIZE = 20;
                break;
            case "Large":
                ARTICLE_TITLE_SIZE = 30;
                ARTICLE_CATEGORY_SIZE = 20;
                ARTICLE_PUBLISH_DATE_SIZE = 20;
                ARTICLE_CONTENT_SIZE = 25;
                break;
        }
    }

    private static void setFeedsRecyclerViewAnimation(Context context, SharedPreferences defaultSharedPreferences) {
        String feedsRecyclerViewAnimationKey = context.getResources().getString(R.string.perf_feeds_anim_key);
        FEEDS_RECYCLER_VIEW_ANIMATION = defaultSharedPreferences.getBoolean(feedsRecyclerViewAnimationKey, true);
    }

    private static void setSourcesRecyclerViewAnimation(Context context, SharedPreferences defaultSharedPreferences) {
        String sourcesRecyclerViewAnimationKey = context.getResources().getString(R.string.perf_sources_anim_key);
        SOURCES_RECYCLER_VIEW_ANIMATION = defaultSharedPreferences.getBoolean(sourcesRecyclerViewAnimationKey, true);
    }

    private static void setCircularReveal(Context context, SharedPreferences defaultSharedPreferences) {
        String circularRevealKey = context.getResources().getString(R.string.perf_circular_reveal_key);
        CIRCULAR_REVEAL = defaultSharedPreferences.getBoolean(circularRevealKey, true);
    }

    private static void setTheme(Context context, SharedPreferences defaultSharedPreferences) {
        String themeKey = context.getResources().getString(R.string.perf_theme_key);
        String theme = defaultSharedPreferences.getString(themeKey, "Light");

        switch (theme) {
            case "Light":
                THEME = true;
                break;
            case "Dark":
                THEME = false;
                break;
        }
    }

    private static void setFeedsCache(Context context, SharedPreferences defaultSharedPreferences) {
        String feedsCacheKey = context.getResources().getString(R.string.perf_cache_key);
        FEED_CACHE = defaultSharedPreferences.getBoolean(feedsCacheKey, true);
    }

    private static void setInAppBrowser(Context context, SharedPreferences defaultSharedPreferences) {
        String inAppBrowserKey = context.getResources().getString(R.string.perf_in_app_browser_key);
        IN_APP_BROWSER = defaultSharedPreferences.getBoolean(inAppBrowserKey, true);
    }

    public static boolean isNewInstall(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_SP, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(NEW_INSTALL, true);
    }

    public static void setNewInstall(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(NEW_INSTALL, false);
        editor.commit();
    }

    public static String getSortingMethod(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_SP, Context.MODE_PRIVATE);
        return sharedPreferences.getString(FEEDS_SORT, "random");
    }

    public static int getSortingMethodPosition(Context context) {
        switch (getSortingMethod(context)) {
            case "random":
                return 0;
            case "feed_title":
                return 1;
            case "feed_category":
                return 2;
            case "feed_pub_date":
                return 3;
            default:
                return -1;
        }
    }

    public static void setSortingMethodPosition(Context context, int position) {
        switch (position) {
            case 0:
                setSortingMethod(context, "random");
                break;
            case 1:
                setSortingMethod(context, "feed_title");
                break;
            case 2:
                setSortingMethod(context, "feed_category");
                break;
            case 3:
                setSortingMethod(context, "feed_pub_date");
                break;
        }
    }

    public static void setSortingMethod(Context context, String type) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FEEDS_SORT, type);
        editor.commit();
    }

    public static void showChangeLog(Context context) {
        MaterialDialog changeLogDialog = new MaterialDialog.Builder(context)
                .title(R.string.change_log)
                .content(R.string.change_log_desc)
                .negativeText(R.string.dismiss)
                .build();
        changeLogDialog.show();
    }

    private static void isChangeDialogToBeShown(Context context) {
        if (!getOldAppVersion(context).equals(getCurrAppVersion(context))) {
            CHANGE_LOG_DIALOG_SHOW = true;
            setAppVersion(context);
        } else {
            CHANGE_LOG_DIALOG_SHOW = false;
        }
    }

    private static void setAppVersion(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_VERSION, getCurrAppVersion(context));
        editor.commit();
    }

    private static String getOldAppVersion(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_SP, Context.MODE_PRIVATE);
        return sharedPreferences.getString(APP_VERSION, "");
    }

    private static String getCurrAppVersion(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packageInfo.versionName;
        return version;
    }
}
