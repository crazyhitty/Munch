package com.crazyhitty.chdev.ks.munch.ui.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crazyhitty.chdev.ks.munch.R;
import com.crazyhitty.chdev.ks.munch.models.LibraryItem;
import com.crazyhitty.chdev.ks.munch.models.SettingsPreferences;
import com.crazyhitty.chdev.ks.munch.ui.adapters.LibrariesRecyclerViewAdapter;
import com.crazyhitty.chdev.ks.munch.utils.WebsiteIntentUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {

    private static final String EXTRA_CUSTOM_TABS_SESSION = "android.support.customtabs.extra.SESSION";
    private static final String EXTRA_CUSTOM_TABS_TOOLBAR_COLOR = "android.support.customtabs.extra.TOOLBAR_COLOR";
    private static String EMAIL_SUBJECT = "Munch Feedback";
    private static String MESSAGE_TYPE = "message/rfc822";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.text_view_app_version)
    TextView txtAppVersion;
    @Bind(R.id.recycler_view_libraries)
    RecyclerView recyclerViewLibraries;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActivityTheme();

        setContentView(R.layout.activity_about);

        ButterKnife.bind(this);

        setToolbar();

        //set the application version
        setAppVersion();

        //set our libraries recycler view
        setLibrariesRecyclerView();
    }

    private void setActivityTheme() {
        if (!SettingsPreferences.THEME) {
            setTheme(R.style.DarkAppTheme_NoActionBar);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(AboutActivity.this, R.color.darkColorPrimaryDark));
            }
            getWindow().setBackgroundDrawableResource(R.color.darkColorBackground);
        }
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setAppVersion() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packageInfo.versionName;
        txtAppVersion.setText("App Version: " + version);
    }

    @OnClick(R.id.frame_layout_contact)
    public void contactDev() {
        CharSequence[] contacts = getResources().getStringArray(R.array.contacts);

        MaterialDialog contactDialog = new MaterialDialog.Builder(AboutActivity.this)
                .title(R.string.contact_dev)
                .items(contacts)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        switch (i) {
                            case 0: //Mail
                                contactViaMail();
                                break;
                            case 1: //Website
                                contactViaWebsite();
                                break;
                            case 2: //Google Plus
                                contactViaGooglePlus();
                                break;
                            case 3: //Github
                                contactViaGithub();
                                break;
                        }
                    }
                }).build();

        contactDialog.show();
    }

    private void contactViaMail() {
        String[] emailId = {getResources().getString(R.string.dev_mail)};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailId);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);
        emailIntent.setType(MESSAGE_TYPE);
        startActivity(emailIntent);
    }

    private void contactViaWebsite() {
        if (SettingsPreferences.IN_APP_BROWSER) {
            new WebsiteIntentUtil(AboutActivity.this).loadCustomChromeTabs(getResources().getString(R.string.dev_website));
        } else {
            new WebsiteIntentUtil(AboutActivity.this).loadNormalBrowser(getResources().getString(R.string.dev_website));
        }
    }

    private void contactViaGooglePlus() {
        if (SettingsPreferences.IN_APP_BROWSER) {
            new WebsiteIntentUtil(AboutActivity.this).loadCustomChromeTabs(getResources().getString(R.string.dev_google_plus));
        } else {
            new WebsiteIntentUtil(AboutActivity.this).loadNormalBrowser(getResources().getString(R.string.dev_google_plus));
        }
    }

    private void contactViaGithub() {
        if (SettingsPreferences.IN_APP_BROWSER) {
            new WebsiteIntentUtil(AboutActivity.this).loadCustomChromeTabs(getResources().getString(R.string.dev_github));
        } else {
            new WebsiteIntentUtil(AboutActivity.this).loadNormalBrowser(getResources().getString(R.string.dev_github));
        }
    }

    private void setLibrariesRecyclerView() {
        mLayoutManager = new LinearLayoutManager(AboutActivity.this);
        recyclerViewLibraries.setHasFixedSize(true);
        recyclerViewLibraries.setLayoutManager(mLayoutManager);
        LibrariesRecyclerViewAdapter librariesRecyclerViewAdapter = new LibrariesRecyclerViewAdapter(AboutActivity.this, new LibraryItem().getLibraryItems(AboutActivity.this));
        recyclerViewLibraries.setAdapter(librariesRecyclerViewAdapter);
    }

}
