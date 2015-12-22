package com.crazyhitty.chdev.ks.munch.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.crazyhitty.chdev.ks.munch.R;

/**
 * Created by Kartik_ch on 12/20/2015.
 */
public class WebsiteIntentUtil {
    private static final String EXTRA_CUSTOM_TABS_SESSION = "android.support.customtabs.extra.SESSION";
    private static final String EXTRA_CUSTOM_TABS_TOOLBAR_COLOR = "android.support.customtabs.extra.TOOLBAR_COLOR";
    private Context mContext;

    public WebsiteIntentUtil(Context mContext) {
        this.mContext = mContext;
    }

    public void loadCustomChromeTabs(String url) {
        //ANDROID CHROME CUSTOM TABS IMPLEMENTATION

        // Using a VIEW intent for compatibility with any other browsers on device.
        // Caller should not be setting FLAG_ACTIVITY_NEW_TASK or
        // FLAG_ACTIVITY_NEW_DOCUMENT.

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        //  Must have. Extra used to match the session. Its value is an IBinder passed
        //  whilst creating a news session. See newSession() below. Even if the service is not
        //  used and there is no valid session id to be provided, this extra has to be present
        //  with a null value to launch a custom tab.

        //CustomTabsClient

        Bundle extras = new Bundle();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            extras.putBinder(EXTRA_CUSTOM_TABS_SESSION, null);
        }
        intent.putExtra(EXTRA_CUSTOM_TABS_TOOLBAR_COLOR, R.color.colorPrimary);
        intent.putExtras(extras);
        mContext.startActivity(intent);
    }

    public void loadNormalBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mContext.startActivity(intent);
    }
}
