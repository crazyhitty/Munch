package com.crazyhitty.chdev.ks.munch.utils;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

import io.codetail.animation.SupportAnimator;

/**
 * Created by Kartik_ch on 11/6/2015.
 */
public class AnimationUtil {
    private Context mContext;
    private View view;
    private SupportAnimator mAnimator;

    public AnimationUtil(Context mContext) {
        this.mContext = mContext;
    }

    public void revealAnimation(final View mainView, final View initView) {
        // get the center for the clipping circle
        int cx = mainView.getRight();
        int cy = mainView.getBottom();

        // get the final radius for the clipping circle
        int dx = Math.max(cx, mainView.getWidth() - cx);
        int dy = Math.max(cy, mainView.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        mAnimator = io.codetail.animation.ViewAnimationUtils.createCircularReveal(mainView, cx, cy, 0, finalRadius);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.setDuration(300);
        mAnimator.start();

        mainView.setVisibility(View.VISIBLE);

        mAnimator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {
                initView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel() {

            }

            @Override
            public void onAnimationRepeat() {

            }
        });
    }

    public void revealAnimationHide(final View mainView, final View initView) {
        // get the center for the clipping circle
        int cx = mainView.getRight();
        int cy = mainView.getBottom();

        // get the final radius for the clipping circle
        int dx = Math.max(cx, mainView.getWidth() - cx);
        int dy = Math.max(cy, mainView.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        mAnimator = io.codetail.animation.ViewAnimationUtils.createCircularReveal(mainView, cx, cy, finalRadius, 0);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.setDuration(300);
        mAnimator.start();

        initView.setVisibility(View.VISIBLE);

        mAnimator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {
                mainView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel() {

            }

            @Override
            public void onAnimationRepeat() {

            }
        });
    }

    public boolean isAnimating() {
        return mAnimator.isRunning();
    }

    //Default circular reveal lags on some devices
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void circularReveal(final View mainView, View initialView, View finalView) {
        float x = initialView.getX() + initialView.getWidth() / 2;
        float y = initialView.getY() + initialView.getHeight() / 2;

        int x1 = (int) x;
        int y1 = (int) y;

        float finalRadius = (float) Math.max(finalView.getWidth(), finalView.getHeight());

        Animator animator = ViewAnimationUtils.createCircularReveal(finalView, x1, y1, 0, finalRadius);
        finalView.setVisibility(View.VISIBLE);
        //animator.setDuration(300);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mainView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    //Default circular reveal lags on some devices
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void circularRevealHide(final View mainView, View initialView, View finalView) {
        float x = initialView.getX() + initialView.getWidth() / 2;
        float y = initialView.getY() + initialView.getHeight() / 2;

        int x1 = (int) x;
        int y1 = (int) y;

        float initialRadius = (float) Math.max(mainView.getWidth(), mainView.getHeight());

        Animator animator = ViewAnimationUtils.createCircularReveal(mainView, x1, y1, initialRadius, 0);
        finalView.setVisibility(View.VISIBLE);
        //animator.setDuration(300);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mainView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

}
