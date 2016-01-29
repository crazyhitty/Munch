package com.crazyhitty.chdev.ks.munch.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crazyhitty.chdev.ks.munch.R;
import com.crazyhitty.chdev.ks.munch.models.Categories;
import com.crazyhitty.chdev.ks.munch.models.CategoryItem;
import com.crazyhitty.chdev.ks.munch.models.SettingsPreferences;
import com.crazyhitty.chdev.ks.munch.models.SourceItem;
import com.crazyhitty.chdev.ks.munch.sources.ISourceView;
import com.crazyhitty.chdev.ks.munch.sources.SourcesPresenter;
import com.crazyhitty.chdev.ks.munch.ui.adapters.CategoryListAdapter;
import com.crazyhitty.chdev.ks.munch.ui.fragments.ArchiveFragment;
import com.crazyhitty.chdev.ks.munch.ui.fragments.FeedsFragment;
import com.crazyhitty.chdev.ks.munch.ui.fragments.ManageSourcesFragment;
import com.crazyhitty.chdev.ks.munch.utils.AnimationUtil;
import com.crazyhitty.chdev.ks.munch.utils.DateUtil;
import com.crazyhitty.chdev.ks.munch.utils.FadeAnimationUtil;
import com.github.clans.fab.FloatingActionMenu;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity implements ISourceView, FloatingActionMenu.OnMenuToggleListener, AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.text_view_toolbar_title)
    TextView txtToolbarTitle;
    @Bind(R.id.recycler_view_feeds)
    RecyclerView recyclerViewFeeds;
    @Bind(R.id.fab)
    FloatingActionMenu fab;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.main_layout)
    RelativeLayout mainLayout;
    @Bind(R.id.secondary_layout)
    RelativeLayout secondaryLayout;
    @Bind(R.id.material_edit_text_source_name)
    MaterialEditText eTxtSourceName;
    @Bind(R.id.material_edit_text_source_url)
    MaterialEditText eTxtSourceUrl;
    @Bind(R.id.button_category)
    Button btnCategory;
    @Bind(R.id.button_save)
    Button btnSave;
    @Bind(R.id.image_view_category)
    ImageView imgCategory;
    @Bind(R.id.text_view_category)
    TextView txtCategory;
    @Bind(R.id.category_layout)
    LinearLayout categoryLayout;
    @Bind(R.id.spinner_sources)
    Spinner spinnerSources;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    int mNavPosition = 0;
    private SourcesPresenter mSourcesPresenter;
    private AnimationUtil mAnimationUtil;
    private DateUtil mDateUtil;
    private SourceItem mSourceItem;
    private boolean mAddFeedStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize settings
        SettingsPreferences.init(HomeActivity.this);

        //set theme
        setActivityTheme();

        setContentView(R.layout.activity_home);

        ButterKnife.bind(HomeActivity.this);

        //set theme of other ui elements
        setUiElementsTheme();

        if (mSourcesPresenter == null) {
            mSourcesPresenter = new SourcesPresenter(HomeActivity.this, HomeActivity.this);
        }

        fab.setOnMenuToggleListener(this);

        setSourcesSpinner();

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        //set the first item enabled true
        navigationView.getMenu().getItem(0).setChecked(true);

        //show changelog if user opens the app for first time
        if (SettingsPreferences.CHANGE_LOG_DIALOG_SHOW) {
            SettingsPreferences.showChangeLog(HomeActivity.this);
        }
    }

    private void setActivityTheme() {
        if (!SettingsPreferences.THEME) {
            setTheme(R.style.DarkAppTheme_NoActionBar);
            getWindow().setBackgroundDrawableResource(R.color.darkColorBackground);
        }
    }

    private void setUiElementsTheme() {
        if (!SettingsPreferences.THEME) {
            fab.setMenuButtonColorNormal(ContextCompat.getColor(HomeActivity.this, R.color.darkColorAccent));
            fab.setMenuButtonColorPressed(ContextCompat.getColor(HomeActivity.this, R.color.darkColorAccent));
            fab.setMenuButtonColorRipple(ContextCompat.getColor(HomeActivity.this, R.color.darkColorAccentDark));
            secondaryLayout.setBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.darkColorAccent));
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_feeds) {
            if (secondaryLayout.getVisibility() == View.INVISIBLE && mNavPosition != 0) {
                mSourcesPresenter.getSources();
                //new FadeAnimationUtil(HomeActivity.this).fadeInAlpha(spinnerSources, 500);
                spinnerSources.setVisibility(View.VISIBLE);
                fragment = new FeedsFragment().setInstance("all_sources");
                fab.showMenuButton(true);
                txtToolbarTitle.setText("Add Feed");
                txtToolbarTitle.setVisibility(View.INVISIBLE);
            }
            mNavPosition = 0;
            //new FadeAnimationUtil(HomeActivity.this).fadeOutAlpha(txtToolbarTitle, 500);
        } else if (id == R.id.nav_manage_sources) {
            hideSpinnerAndFab();
            fragment = new ManageSourcesFragment();
            showTitle("Manage Sources");
            mNavPosition = 1;
        } else if (id == R.id.nav_archive) {
            hideSpinnerAndFab();
            fragment = new ArchiveFragment();
            showTitle("Archive");
            mNavPosition = 2;
        } else if (id == R.id.nav_settings) {
            runIntent(SettingsActivity.class);
            mNavPosition = 3;
        } else if (id == R.id.nav_about) {
            runIntent(AboutActivity.class);
            mNavPosition = 4;
        }

        if (fragment != null) {
            loadFragment(fragment);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void runIntent(final Class resultActivityClass) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(HomeActivity.this, resultActivityClass);
                startActivity(intent);
            }
        }, 200);
    }

    private void showTitle(String title) {
        txtToolbarTitle.setText(title);
        if (txtToolbarTitle.getVisibility() != View.VISIBLE) {
            new FadeAnimationUtil(HomeActivity.this).fadeInAlpha(txtToolbarTitle, 500);
        }
    }

    private void hideSpinnerAndFab() {
        if (spinnerSources.getVisibility() != View.INVISIBLE || secondaryLayout.getVisibility() != View.INVISIBLE) {
            new FadeAnimationUtil(HomeActivity.this).fadeOutAlpha(spinnerSources, 500);
        }
        //spinnerSources.setVisibility(View.INVISIBLE);
        fab.hideMenuButton(true);
    }

    //set the selected fragment onto the screen(activity)
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.main_layout, fragment);
        fragmentTransaction.commit();
    }

    private void setSourcesSpinner() {
        mSourcesPresenter.getSources();
        spinnerSources.setOnItemSelectedListener(this);
    }

    @Override
    public void onMenuToggle(boolean opened) {
        mAnimationUtil = new AnimationUtil(HomeActivity.this);
        fab.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fab.setEnabled(true);
            }
        }, 500);
        if (opened) {
            showAddSourceScreen(true);
        } else {
            showAddSourceScreen(false);
        }
    }

    private void showAddSourceScreen(boolean status) {
        enableSecondaryLayout(status);
        if (status) {
            new FadeAnimationUtil(HomeActivity.this).fadeInAlpha(txtToolbarTitle, 500);
            new FadeAnimationUtil(HomeActivity.this).fadeOutAlpha(spinnerSources, 500);
            //new FadeAnimationUtil(HomeActivity.this).fadeOutAlpha(toolbar, 500);
            if (SettingsPreferences.CIRCULAR_REVEAL) {
                mAnimationUtil.revealAnimation(secondaryLayout, mainLayout);
            } else {
                new FadeAnimationUtil(HomeActivity.this).fadeOutAlpha(mainLayout, 500);
                new FadeAnimationUtil(HomeActivity.this).fadeInAlpha(secondaryLayout, 500);
            }
        } else {
            if (mNavPosition == 0) {
                new FadeAnimationUtil(HomeActivity.this).fadeOutAlpha(txtToolbarTitle, 500);
                new FadeAnimationUtil(HomeActivity.this).fadeInAlpha(spinnerSources, 500);
            }
            //new FadeAnimationUtil(HomeActivity.this).fadeInAlpha(toolbar, 500);
            if (SettingsPreferences.CIRCULAR_REVEAL) {
                mAnimationUtil.revealAnimationHide(secondaryLayout, mainLayout);
            } else {
                new FadeAnimationUtil(HomeActivity.this).fadeOutAlpha(secondaryLayout, 500);
                new FadeAnimationUtil(HomeActivity.this).fadeInAlpha(mainLayout, 500);
            }
        }
        mAddFeedStatus = status;
    }

    @OnClick(R.id.button_save)
    public void saveSource() {
        String sourceName = eTxtSourceName.getText().toString();
        String sourceUrl = eTxtSourceUrl.getText().toString();
        String sourceCategory = txtCategory.getText().toString();
        //Toast.makeText(HomeActivity.this, "name: "+sourceName+", url: "+sourceUrl, Toast.LENGTH_SHORT).show();
        mDateUtil = new DateUtil();
        String date = mDateUtil.getCurrDate();

        mSourceItem = new SourceItem();
        mSourceItem.setSourceName(sourceName);
        mSourceItem.setSourceUrl(sourceUrl);
        mSourceItem.setSourceCategoryName(sourceCategory);
        mSourceItem.setSourceCategoryImgId(new Categories(HomeActivity.this).getDrawableId(sourceCategory));
        mSourceItem.setSourceDateAdded(date);

        mSourcesPresenter.addSource(mSourceItem);
    }

    @OnClick(R.id.button_category)
    public void showCategory() {
        final List<CategoryItem> categoryItems = new Categories(HomeActivity.this).getCategoryItems();

        final MaterialDialog categoryDialog = new MaterialDialog.Builder(HomeActivity.this)
                .title(R.string.add_category)
                .adapter(new CategoryListAdapter(HomeActivity.this, categoryItems),
                        new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                //Toast.makeText(HomeActivity.this, "Clicked item " + which, Toast.LENGTH_SHORT).show();
                                imgCategory.setImageDrawable(categoryItems.get(which).getCategoryImg());
                                txtCategory.setText(categoryItems.get(which).getCategoryName());
                                categoryLayout.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            }
                        })
                .build();
        categoryDialog.show();
    }

    // spinner Items (select from different sources)
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //Toast.makeText(HomeActivity.this, "Item selected: " + adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
        if (i == 0) {
            loadFragment(new FeedsFragment().setInstance("all_sources"));
        } else {
            loadFragment(new FeedsFragment().setInstance(adapterView.getItemAtPosition(i).toString()));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void enableSecondaryLayout(boolean status) {
        eTxtSourceName.setEnabled(status);
        eTxtSourceUrl.setEnabled(status);
        btnCategory.setEnabled(status);
        btnSave.setEnabled(status);
        //categoryLayout.setVisibility(View.INVISIBLE);
        /*if(status){
            categoryLayout.setVisibility(View.VISIBLE);
        }else {
            categoryLayout.setVisibility(View.INVISIBLE);
        }*/
    }

    @Override
    public void dataSourceSaved(String message) {
        //add newly added items to spinner. Not working currently
        mSourcesPresenter.getSources();

        Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
        fab.close(true);
        enableSecondaryLayout(false);
        //If called instantly after save button is clicked, will make the reveal(hide) animation lag a little bit,
        //so run the animation after a certain period of time.
        //mAnimationUtil.revealAnimationHide(secondaryLayout, mainLayout);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //new FadeAnimationUtil(HomeActivity.this).fadeInAlpha(toolbar, 500);
                drawer.setEnabled(true);
                if (SettingsPreferences.CIRCULAR_REVEAL) {
                    mAnimationUtil.revealAnimationHide(secondaryLayout, mainLayout);
                } else {
                    new FadeAnimationUtil(HomeActivity.this).fadeOutAlpha(secondaryLayout, 500);
                    new FadeAnimationUtil(HomeActivity.this).fadeInAlpha(mainLayout, 500);
                }
            }
        }, 500);
        mAddFeedStatus = false;
        categoryLayout.setVisibility(View.INVISIBLE);
        clearSourceValues();
    }

    @Override
    public void dataSourceSaveFailed(String message) {
        if (message.equals("name_url_category_empty")) {
            message = "Name, url and category cannot be empty";
            eTxtSourceName.setError("Enter a valid name");
            eTxtSourceUrl.setError("Enter a valid url");
        } else if (message.equals("name_empty")) {
            message = "Name cannot be empty";
            eTxtSourceName.setError("Enter a valid name");
        } else if (message.equals("url_empty")) {
            message = "Url cannot be empty";
            eTxtSourceUrl.setError("Enter a valid url");
        } else if (message.equals("category_empty")) {
            message = "Category cannot be empty";
        } else if (message.equals("incorrect_url")) {
            message = "Invalid url";
            eTxtSourceUrl.setError("Enter a valid url");
        }
        Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dataSourceLoaded(List<String> sourceNames) {
        //Toast.makeText(HomeActivity.this, "data source loaded", Toast.LENGTH_SHORT).show();
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(HomeActivity.this, R.layout.spinner_text, sourceNames);
        spinnerSources.setAdapter(adapter);
    }

    //do not use it
    @Override
    public void dataSourceItemsLoaded(List<SourceItem> sourceItems) {

    }

    @Override
    public void dataSourceLoadingFailed(String message) {
        Toast.makeText(HomeActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    //No use
    @Override
    public void sourceItemModified(SourceItem sourceItem, String oldName) {

    }

    //No use
    @Override
    public void sourceItemModificationFailed(String message) {

    }

    //No use
    @Override
    public void sourceItemDeleted(SourceItem sourceItem) {

    }

    //No use
    @Override
    public void sourceItemDeletionFailed(String message) {

    }

    private void clearSourceValues() {
        eTxtSourceName.setText("");
        eTxtSourceUrl.setText("");
        imgCategory.setImageDrawable(null);
        txtCategory.setText(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mAddFeedStatus) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //new FadeAnimationUtil(HomeActivity.this).fadeInAlpha(toolbar, 500);
                    if (SettingsPreferences.CIRCULAR_REVEAL) {
                        mAnimationUtil.revealAnimationHide(secondaryLayout, mainLayout);
                    } else {
                        new FadeAnimationUtil(HomeActivity.this).fadeOutAlpha(secondaryLayout, 500);
                        new FadeAnimationUtil(HomeActivity.this).fadeInAlpha(mainLayout, 500);
                    }
                }
            }, 500);
            mAddFeedStatus = false;
            fab.close(true);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feeds, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_clear_feeds) {
            //mFeedsPresenter.deleteFeeds();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
