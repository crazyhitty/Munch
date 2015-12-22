package com.crazyhitty.chdev.ks.munch.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by Kartik_ch on 8/17/2015.
 */
public class FadeAnimationUtil {

    private Context mContext;

    public FadeAnimationUtil(Context context) {
        this.mContext = context;
    }

    public void fadeInLeft(View view) {
        view.setVisibility(View.INVISIBLE);
        Animation fadeInAnim = AnimationUtils.makeInAnimation(mContext, true);
        view.startAnimation(fadeInAnim);
        view.setVisibility(View.VISIBLE);
        Log.d("fadeIn", "true");
    }

    public void fadeOutRight(View view) {
        Animation fadeOutAnim = AnimationUtils.makeOutAnimation(mContext, true);
        view.startAnimation(fadeOutAnim);
        view.setVisibility(View.INVISIBLE);
        Log.d("fadeOut", "true");
    }

    public void fadeInAlpha(View view, int durationInMillis) {
        view.setVisibility(View.INVISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1.0f);
        alphaAnimation.setDuration(durationInMillis);
        view.startAnimation(alphaAnimation);
        view.setVisibility(View.VISIBLE);
    }

    public void fadeOutAlpha(final View view, int durationInMillis) {
        view.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
        alphaAnimation.setDuration(durationInMillis);
        view.startAnimation(alphaAnimation);
        view.setVisibility(View.INVISIBLE);
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
            }
        }, 1000);*/
    }
}
