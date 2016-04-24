package com.crazyhitty.chdev.ks.munch.ui.activities;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.folderselector.FileChooserDialog;
import com.crazyhitty.chdev.ks.munch.R;
import com.crazyhitty.chdev.ks.munch.curatedfeeds.CuratedFeedsPresenter;
import com.crazyhitty.chdev.ks.munch.curatedfeeds.ICuratedFeedsView;
import com.crazyhitty.chdev.ks.munch.importopml.IImportOpmlView;
import com.crazyhitty.chdev.ks.munch.importopml.ImportOpmlPresenter;
import com.crazyhitty.chdev.ks.munch.models.SettingsPreferences;
import com.crazyhitty.chdev.ks.munch.models.SourceItem;
import com.crazyhitty.chdev.ks.munch.sources.ISourceView;
import com.crazyhitty.chdev.ks.munch.sources.SourcesPresenter;
import com.crazyhitty.chdev.ks.munch.utils.FadeAnimationUtil;
import com.crazyhitty.chdev.ks.munch.utils.NetworkConnectionUtil;
import com.crazyhitty.chdev.ks.munch.utils.WebsiteIntentUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatActivity implements FileChooserDialog.FileCallback {

    public static FileChooserDialog sFileChooserDialog;
    private static MaterialProgressBar mMaterialProgressBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActivityTheme();
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        //bind progress bar
        mMaterialProgressBar = (MaterialProgressBar) findViewById(R.id.indeterminate_progress);
        if (!NetworkConnectionUtil.isNetworkAvailable(SettingsActivity.this)) {
            mMaterialProgressBar.setVisibility(View.GONE);
        }

        setToolBar();

        getFragmentManager().beginTransaction().replace(R.id.frame_layout_settings, new SettingsFragment()).commit();

        sFileChooserDialog = new FileChooserDialog.Builder(SettingsActivity.this)
                .cancelButton(R.string.cancel)
                .build();
    }

    private void setActivityTheme() {
        if (!SettingsPreferences.THEME) {
            setTheme(R.style.DarkAppTheme_NoActionBar);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(SettingsActivity.this, R.color.darkColorPrimaryDark));
            }
            getWindow().setBackgroundDrawableResource(R.color.darkColorBackground);
        }
    }

    private void setToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onFileSelection(FileChooserDialog dialog, File file) {
        ImportOpmlPresenter.onFileSelected(file);
    }

    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener, ICuratedFeedsView, ISourceView, IImportOpmlView {
        private final static int STORAGE_PERMISSION_RC = 1;
        private static String EMAIL_SUBJECT = "Custom curated list - Munch";
        private static String MESSAGE_TYPE = "message/rfc822";
        private static String SOURCE_NAME = "source_name";
        private static String SOURCE_URL = "source_url";
        private static String SOURCE_CATEGORY = "source_category";
        private static String FEEDS_NOT_WORKING_TUTORIAL_URL = "http://crazyhitty.com/blog/2016/january/make-your-feeds-work-with-munch.html";
        private CuratedFeedsPresenter mCuratedFeedsPresenter;
        private SourcesPresenter mSourcesPresenter;
        private Preference mFeedsNotWorkingPreference;
        private ListPreference mOpmlFilePreference;
        private MultiSelectListPreference mCuratedFeedsPreference, mCuratedFeedsSubmitPreference;
        private List<SourceItem> mCuratedSourceItems, mAvailableSourceItems;
        private ImportOpmlPresenter mImportOpmlPresenter;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            //bind the preference views
            bindPreferences();

            //add preference click listener
            mCuratedFeedsPreference.setOnPreferenceClickListener(this);
            //add preference change listener
            mCuratedFeedsPreference.setOnPreferenceChangeListener(this);

            if (mCuratedFeedsPresenter == null) {
                mCuratedFeedsPresenter = new CuratedFeedsPresenter(this);
            }

            if (mSourcesPresenter == null) {
                mSourcesPresenter = new SourcesPresenter(this, getActivity());
            }

            if (NetworkConnectionUtil.isNetworkAvailable(getActivity())) {
                //load feeds from the web server if internet connection is available
                mCuratedFeedsPresenter.attemptCuratedFeedsLoading(getActivity());
            }

            if (mImportOpmlPresenter == null) {
                mImportOpmlPresenter = new ImportOpmlPresenter(this, getActivity());
            }

            //load available sources
            mSourcesPresenter.getSourceItems();

            //add preference change listener
            mCuratedFeedsSubmitPreference.setOnPreferenceChangeListener(this);

            //add preference change listener
            mOpmlFilePreference.setOnPreferenceChangeListener(this);

            //add preference click listener
            mFeedsNotWorkingPreference.setOnPreferenceClickListener(this);
        }

        private void bindPreferences() {
            mCuratedFeedsPreference = (MultiSelectListPreference) findPreference(getActivity().getResources().getString(R.string.perf_curated_feeds_import_key));
            mCuratedFeedsSubmitPreference = (MultiSelectListPreference) findPreference(getActivity().getResources().getString(R.string.perf_curated_feeds_submit_key));
            mOpmlFilePreference = (ListPreference) findPreference(getActivity().getResources().getString(R.string.perf_import_opml_key));
            mFeedsNotWorkingPreference = findPreference(getActivity().getResources().getString(R.string.perf_feeds_not_working_key));
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (preference.getKey().equals(getActivity().getResources().getString(R.string.perf_curated_feeds_import_key))) {
                if (mCuratedFeedsPreference.getEntries().length == 0) {
                    mCuratedFeedsPreference.getDialog().dismiss();
                    if (!NetworkConnectionUtil.isNetworkAvailable(getActivity())) {
                        Toast.makeText(getActivity(), "Unable to download curated feeds", Toast.LENGTH_SHORT).show();
                        NetworkConnectionUtil.showNoNetworkDialog(getActivity());
                    } else {
                        Toast.makeText(getActivity(), "Curated feeds not available, please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            if (preference.getKey().equals(getActivity().getResources().getString(R.string.perf_feeds_not_working_key))) {
                if (SettingsPreferences.IN_APP_BROWSER) {
                    new WebsiteIntentUtil(getActivity()).loadCustomChromeTabs(FEEDS_NOT_WORKING_TUTORIAL_URL);
                } else {
                    new WebsiteIntentUtil(getActivity()).loadNormalBrowser(FEEDS_NOT_WORKING_TUTORIAL_URL);
                }
            }

            return false;
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object updatedObject) {
            if (preference.getKey().equals(getActivity().getResources().getString(R.string.perf_curated_feeds_import_key))) {
                Set<String> selections = (Set<String>) updatedObject;
                manageCuratedSources(selections);
            }

            if (preference.getKey().equals(getActivity().getResources().getString(R.string.perf_curated_feeds_submit_key))) {
                Set<String> selections = (Set<String>) updatedObject;
                sendCuratedSources(selections);
            }

            if (preference.getKey().equals(getActivity().getResources().getString(R.string.perf_import_opml_key))) {
                String selection = (String) updatedObject;
                if (selection.equals("From url")) {
                    mImportOpmlPresenter.attemptFeedsRetrievalFromOpml(true);
                } else if (selection.equals("From file")) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_RC);
                        return false;
                    }
                    mImportOpmlPresenter.attemptFeedsRetrievalFromOpml(false);
                }
            }

            //false is returned so that the preference is not saved
            return false;
        }

        private void manageCuratedSources(Set<String> selections) {
            Iterator<String> iterator = selections.iterator();
            while (iterator.hasNext()) {
                String selectedVal = iterator.next();
                for (SourceItem sourceItem : mCuratedSourceItems) {
                    if (sourceItem.getSourceName().equals(selectedVal)) {
                        mSourcesPresenter.addSource(sourceItem);
                    }
                }
            }
        }

        private void sendCuratedSources(Set<String> selections) {
            List<SourceItem> customCuratedSourceItems = new ArrayList<>();

            Iterator<String> iterator = selections.iterator();
            while (iterator.hasNext()) {
                String selectedVal = iterator.next();
                for (SourceItem sourceItem : mAvailableSourceItems) {
                    if (sourceItem.getSourceName().equals(selectedVal)) {
                        customCuratedSourceItems.add(sourceItem);
                    }
                }
            }

            sendCustomCuratedSourcesMail(customCuratedSourceItems);
        }

        private void sendCustomCuratedSourcesMail(List<SourceItem> customCuratedSourceItems) {
            JSONArray jsonArraySources = new JSONArray();
            for (SourceItem sourceItem : customCuratedSourceItems) {
                JSONObject jsonObjectSource = new JSONObject();
                try {
                    jsonObjectSource.put(SOURCE_NAME, sourceItem.getSourceName());
                    jsonObjectSource.put(SOURCE_URL, sourceItem.getSourceUrl());
                    jsonObjectSource.put(SOURCE_CATEGORY, sourceItem.getSourceCategoryName());
                    jsonArraySources.put(jsonObjectSource);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            String message = null;
            if (jsonArraySources != null) {
                if (jsonArraySources.length() != 0) {
                    message = jsonArraySources.toString();
                }
            }

            if (message != null) {
                message = "Include these feeds into the curated list: \n\n" + message;

                String[] emailId = {getResources().getString(R.string.dev_mail)};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, emailId);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);
                emailIntent.putExtra(Intent.EXTRA_TEXT, message);
                emailIntent.setType(MESSAGE_TYPE);
                startActivity(emailIntent);
            } else {
                Toast.makeText(getActivity(), "Please select a source", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFeedsLoaded(List<SourceItem> sourceItems) {
            new FadeAnimationUtil(getActivity()).fadeOutAlpha(mMaterialProgressBar, 500);

            this.mCuratedSourceItems = sourceItems;

            CharSequence[] sources = new CharSequence[sourceItems.size()];
            for (int i = 0; i < sourceItems.size(); i++) {
                sources[i] = sourceItems.get(i).getSourceName();
            }
            mCuratedFeedsPreference.setEntries(sources);
            mCuratedFeedsPreference.setEntryValues(sources);
        }

        @Override
        public void onFeedsLoadingFailure(String message) {
            Toast.makeText(getActivity(), "Error:\n" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void dataSourceSaved(String message) {
            Log.e("Source Saved", message);
        }

        @Override
        public void dataSourceSaveFailed(String message) {
            Log.e("Source Saving Failed", message);
        }

        //no use
        @Override
        public void dataSourceLoaded(List<String> sourceNames) {

        }

        @Override
        public void dataSourceItemsLoaded(List<SourceItem> sourceItems) {
            this.mAvailableSourceItems = sourceItems;

            CharSequence[] sources = new CharSequence[sourceItems.size()];
            for (int i = 0; i < sourceItems.size(); i++) {
                sources[i] = sourceItems.get(i).getSourceName();
            }
            mCuratedFeedsSubmitPreference.setEntries(sources);
            mCuratedFeedsSubmitPreference.setEntryValues(sources);
        }

        //no use
        @Override
        public void dataSourceLoadingFailed(String message) {

        }

        //no use
        @Override
        public void sourceItemModified(SourceItem sourceItem, String oldName) {

        }

        //no use
        @Override
        public void sourceItemModificationFailed(String message) {

        }

        //no use
        @Override
        public void sourceItemDeleted(SourceItem sourceItem) {

        }

        //no use
        @Override
        public void sourceItemDeletionFailed(String message) {

        }

        @Override
        public void opmlFeedsRetrieved(final List<SourceItem> sourceItems) {
            CharSequence[] titles = new CharSequence[sourceItems.size()];
            for (int i = 0; i < sourceItems.size(); i++) {
                titles[i] = sourceItems.get(i).getSourceName();
            }
            MaterialDialog opmlFeedsDialog = new MaterialDialog.Builder(getActivity())
                    .title(R.string.select_feeds)
                    .items(titles)
                    .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                            for (int i = 0; i < which.length; i++) {
                                mSourcesPresenter.addSource(sourceItems.get(which[i]));
                            }
                            return false;
                        }
                    }).positiveText(R.string.import_str)
                    .negativeText(R.string.cancel).build();
            opmlFeedsDialog.show();
        }

        @Override
        public void opmlFeedsRetrievalFailed(String message) {
            Toast.makeText(getActivity(), "Error:\n" + message, Toast.LENGTH_SHORT).show();
        }
    }
}
