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
import android.view.Menu;
import android.view.MenuItem;
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
        recyclerViewLibraries.setNestedScrollingEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rate) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.crazyhitty.chdev.ks.munch"));
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_change_log) {
            SettingsPreferences.showChangeLog(AboutActivity.this);
        }

        if (id == R.id.action_source_code) {
            if (SettingsPreferences.IN_APP_BROWSER) {
                new WebsiteIntentUtil(AboutActivity.this).loadCustomChromeTabs(getResources().getString(R.string.github_source_code));
            } else {
                new WebsiteIntentUtil(AboutActivity.this).loadNormalBrowser(getResources().getString(R.string.github_source_code));
            }
            return true;
        }

        if (id == R.id.action_contributions) {
            MaterialDialog contributionsDialog = new MaterialDialog.Builder(AboutActivity.this)
                    .title(R.string.contributions)
                    .items(R.array.contributions_arr)
                    .content(R.string.contributions_desc)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            if (SettingsPreferences.IN_APP_BROWSER) {
                                new WebsiteIntentUtil(AboutActivity.this).loadCustomChromeTabs(getResources().getStringArray(R.array.contributions_links_arr)[which]);
                            } else {
                                new WebsiteIntentUtil(AboutActivity.this).loadNormalBrowser(getResources().getStringArray(R.array.contributions_links_arr)[which]);
                            }
                        }
                    })
                    .negativeText(R.string.dismiss)
                    .build();
            contributionsDialog.show();
        }

        if (id == R.id.action_licence) {
            MaterialDialog licenceDialog = new MaterialDialog.Builder(AboutActivity.this)
                    .title(R.string.licence)
                    .content(R.string.licence_desc)
                    .negativeText(R.string.dismiss)
                    .build();
            licenceDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

}
