package com.crazyhitty.chdev.ks.munch.ui.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.ListPreference;
import android.util.AttributeSet;

import com.crazyhitty.chdev.ks.munch.ui.activities.HomeActivity;

/**
 * Created by Kartik_ch on 12/23/2015.
 */
public class CustomListPreferenceDialog extends ListPreference {

    public CustomListPreferenceDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);
        Intent intent = new Intent(getContext(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getContext().startActivity(intent);
    }
}
