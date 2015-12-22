package com.crazyhitty.chdev.ks.munch.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crazyhitty.chdev.ks.munch.R;

/**
 * Created by Kartik_ch on 12/17/2015.
 */
public class NetworkConnectionUtil {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static void showNoNetworkDialog(final Context context) {
        MaterialDialog noNetworkAvailDialog = new MaterialDialog.Builder(context)
                .title(R.string.no_internet_available)
                .content(R.string.no_internet_available_extnd)
                .iconRes(R.drawable.ic_error_24dp)
                .positiveText(R.string.settings)
                .negativeText(R.string.dismiss)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        context.startActivity(intent);
                    }
                }).build();
        noNetworkAvailDialog.show();
    }
}
