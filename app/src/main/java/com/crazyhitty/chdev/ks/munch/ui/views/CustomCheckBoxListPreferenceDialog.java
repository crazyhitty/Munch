package com.crazyhitty.chdev.ks.munch.ui.views;

import android.content.Context;
import android.preference.MultiSelectListPreference;
import android.util.AttributeSet;

/**
 * Created by Kartik_ch on 12/29/2015.
 */
public class CustomCheckBoxListPreferenceDialog extends MultiSelectListPreference {
    public CustomCheckBoxListPreferenceDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setOnPreferenceChangeListener(OnPreferenceChangeListener onPreferenceChangeListener) {
        super.setOnPreferenceChangeListener(onPreferenceChangeListener);
        //Toas
    }
}
