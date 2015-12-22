package com.crazyhitty.chdev.ks.mindb;

import android.database.Cursor;

import java.util.List;

/**
 * Created by Kartik_ch on 11/16/2015.
 */
public interface OnDatabaseListener {
    void onSuccess(String message, List<String> values, Cursor cursor);

    void onFailure(String message);
}
