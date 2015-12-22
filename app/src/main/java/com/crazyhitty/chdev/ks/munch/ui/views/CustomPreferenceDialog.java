package com.crazyhitty.chdev.ks.munch.ui.views;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.munch.utils.DatabaseUtil;

/**
 * Created by Kartik_ch on 12/16/2015.
 */
public class CustomPreferenceDialog extends DialogPreference {
    public CustomPreferenceDialog(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // When the user selects "OK", persist the new value
        if (positiveResult) {
            // User selected OK
            try {
                new DatabaseUtil(getContext()).deleteAll();
                Toast.makeText(getContext(), "Database reset", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            // User selected Cancel
        }
    }

}
