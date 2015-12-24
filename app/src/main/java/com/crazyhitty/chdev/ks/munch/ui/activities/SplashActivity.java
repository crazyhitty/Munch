package com.crazyhitty.chdev.ks.munch.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.crazyhitty.chdev.ks.munch.R;
import com.crazyhitty.chdev.ks.munch.models.SettingsPreferences;
import com.crazyhitty.chdev.ks.munch.ui.adapters.SplashPagerAdapter;
import com.crazyhitty.chdev.ks.munch.utils.FadePageTransformerUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.container)
    ViewPager viewPager;
    @Bind(R.id.image_button_next)
    ImageButton imgBtnNext;

    private SplashPagerAdapter mSplashPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        runOnce();

        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSplashPagerAdapter = new SplashPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mSplashPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        //set page transformer
        viewPager.setPageTransformer(false, new FadePageTransformerUtil());

        viewPager.addOnPageChangeListener(this);
    }

    private void runOnce() {
        if (!SettingsPreferences.isNewInstall(SplashActivity.this)) {
            runIntent(HomeActivity.class);
            finish();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                imgBtnNext.setImageResource(R.drawable.ic_arrow_forward_24dp);
                break;
            case 1:
                imgBtnNext.setImageResource(R.drawable.ic_arrow_forward_24dp);
                break;
            case 2:
                imgBtnNext.setImageResource(R.drawable.ic_done_24dp);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick(R.id.image_button_next)
    public void OnNext() {
        if (viewPager.getCurrentItem() == 2) {
            SettingsPreferences.setNewInstall(SplashActivity.this);
            runIntent(HomeActivity.class);
            finish();
        } else if (viewPager.getCurrentItem() < 2) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
        }
    }

    private void runIntent(Class resultActivity) {
        Intent intent = new Intent(SplashActivity.this, resultActivity);
        startActivity(intent);
    }

}
