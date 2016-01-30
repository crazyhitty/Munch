package com.crazyhitty.chdev.ks.munch.sources;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crazyhitty.chdev.ks.munch.R;
import com.crazyhitty.chdev.ks.munch.models.Categories;
import com.crazyhitty.chdev.ks.munch.models.CategoryItem;
import com.crazyhitty.chdev.ks.munch.models.SettingsPreferences;
import com.crazyhitty.chdev.ks.munch.models.SourceItem;
import com.crazyhitty.chdev.ks.munch.ui.adapters.CategoryListAdapter;

import java.util.List;

/**
 * Created by Kartik_ch on 11/7/2015.
 */
public class SourcesPresenter implements ISourcePresenter, OnSourceSavedListener, OnSourcesLoadedListener, OnSourcesModifyListener {

    private ISourceView mISourceView;
    private SourceInteractor mSourceInteractor;
    private EditText mETxtSourceName, mETxtSourceUrl;
    private TextView mTxtCategory;
    private ImageView mImgCategory;
    private FrameLayout mFrameCategory;

    public SourcesPresenter(ISourceView mISourceView, Context mContext) {
        this.mISourceView = mISourceView;
        this.mSourceInteractor = new SourceInteractor(mContext);
    }

    public void addSource(SourceItem sourceItem) {
        mSourceInteractor.addSourceToDb(this, sourceItem);
    }

    public void getSources() {
        mSourceInteractor.getSourcesFromDb(this);
    }

    public void getSourceItems() {
        mSourceInteractor.getSourceItemsFromDb(this);
    }

    public void modifySources(final Context context, final SourceItem sourceItem) {
        final MaterialDialog modifyDialog = new MaterialDialog.Builder(context)
                .title(R.string.modify_source)
                .customView(R.layout.dialog_modify_source, true)
                .positiveText(R.string.modify)
                .negativeText(R.string.cancel)
                .neutralText(R.string.delete)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        String sourceName = mETxtSourceName.getText().toString();
                        String sourceUrl = mETxtSourceUrl.getText().toString();
                        String sourceCategory = mTxtCategory.getText().toString();
                        int sourceCategoryImgId = new Categories(context).getDrawableId(sourceCategory);

                        SourceItem sourceItemNew = new SourceItem();
                        sourceItemNew.setSourceName(sourceName);
                        sourceItemNew.setSourceUrl(sourceUrl);
                        sourceItemNew.setSourceCategoryName(sourceCategory);
                        sourceItemNew.setSourceCategoryImgId(sourceCategoryImgId);

                        mSourceInteractor.editSourceItemInDb(SourcesPresenter.this, sourceItemNew, sourceItem.getSourceName());
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        mSourceInteractor.deleteSourceItemFromDb(SourcesPresenter.this, sourceItem);
                    }
                })
                .build();

        mETxtSourceName = (EditText) modifyDialog.getView().findViewById(R.id.edit_text_source_name);
        mETxtSourceUrl = (EditText) modifyDialog.getView().findViewById(R.id.edit_text_source_url);
        mFrameCategory = (FrameLayout) modifyDialog.getView().findViewById(R.id.frame_layout_category);
        mTxtCategory = (TextView) modifyDialog.getView().findViewById(R.id.text_view_category);
        mImgCategory = (ImageView) modifyDialog.getView().findViewById(R.id.image_view_category);

        mETxtSourceName.setText(sourceItem.getSourceName());
        mETxtSourceUrl.setText(sourceItem.getSourceUrl());
        mTxtCategory.setText(sourceItem.getSourceCategoryName());
        mImgCategory.setImageResource(new Categories(context).getDrawableId(sourceItem.getSourceCategoryName()));

        //add a white color filter to the images if dark theme is selected
        if (!SettingsPreferences.THEME) {
            mImgCategory.setColorFilter(ContextCompat.getColor(context, R.color.md_grey_100));
        }

        mFrameCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final List<CategoryItem> categoryItems = new Categories(context).getCategoryItems();

                final MaterialDialog categoryDialog = new MaterialDialog.Builder(context)
                        .title(R.string.add_category)
                        .adapter(new CategoryListAdapter(context, categoryItems),
                                new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                        //Toast.makeText(HomeActivity.this, "Clicked item " + which, Toast.LENGTH_SHORT).show();
                                        mImgCategory.setImageDrawable(categoryItems.get(which).getCategoryImg());
                                        mTxtCategory.setText(categoryItems.get(which).getCategoryName());
                                        dialog.dismiss();
                                    }
                                })
                        .build();
                categoryDialog.show();
            }
        });

        modifyDialog.show();
    }

    public void deleteSource(SourceItem sourceItem) {
        mSourceInteractor.deleteSourceItemFromDb(SourcesPresenter.this, sourceItem);
    }

    @Override
    public void onSuccess(String message) {
        mISourceView.dataSourceSaved(message);

    }

    @Override
    public void onFailure(String message) {
        mISourceView.dataSourceSaveFailed(message);
    }

    @Override
    public void onSourceLoaded(List<String> sourceNames) {
        mISourceView.dataSourceLoaded(sourceNames);
    }

    @Override
    public void onSourceItemsLoaded(List<SourceItem> sourceItems) {
        mISourceView.dataSourceItemsLoaded(sourceItems);
    }

    @Override
    public void onSourceLoadingFailed(String message) {
        mISourceView.dataSourceLoadingFailed(message);
    }

    @Override
    public void onSourceModified(SourceItem sourceItem, String oldName) {
        mISourceView.sourceItemModified(sourceItem, oldName);
    }

    @Override
    public void onSourceModifiedFailed(String message) {
        mISourceView.sourceItemModificationFailed(message);
    }

    @Override
    public void onSourceDeleted(SourceItem sourceItem) {
        mISourceView.sourceItemDeleted(sourceItem);
    }

    @Override
    public void onSourceDeletionFailed(String message) {
        mISourceView.sourceItemDeletionFailed(message);
    }
}
