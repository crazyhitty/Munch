package com.crazyhitty.chdev.ks.munch.ui.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.munch.R;
import com.crazyhitty.chdev.ks.munch.models.Categories;
import com.crazyhitty.chdev.ks.munch.models.SettingsPreferences;
import com.crazyhitty.chdev.ks.munch.models.SourceItem;
import com.crazyhitty.chdev.ks.munch.sources.ISourceView;
import com.crazyhitty.chdev.ks.munch.sources.SourcesPresenter;
import com.crazyhitty.chdev.ks.munch.utils.FadeAnimationUtil;
import com.crazyhitty.chdev.ks.munch.utils.UrlUtil;

import java.util.List;

/**
 * Created by Kartik_ch on 12/10/2015.
 */
public class SourcesRecyclerViewAdapter extends RecyclerView.Adapter<SourcesRecyclerViewAdapter.SourcesViewHolder> {
    private Context mContext;
    private List<SourceItem> mSourceItems;
    private SourcesPresenter mSourcesPresenter;
    private int mLastPosition = -1;

    public SourcesRecyclerViewAdapter(Context mContext, List<SourceItem> mSourceItems) {
        this.mContext = mContext;
        this.mSourceItems = mSourceItems;
    }

    @Override
    public SourcesRecyclerViewAdapter.SourcesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.source_item, parent, false);
        SourcesViewHolder sourcesViewHolder = new SourcesViewHolder(view);
        return sourcesViewHolder;
    }

    @Override
    public void onBindViewHolder(SourcesRecyclerViewAdapter.SourcesViewHolder holder, int position) {
        holder.mTxtSource.setText(mSourceItems.get(position).getSourceName());
        holder.mTxtSourceUrl.setText(UrlUtil.getWebsiteName(mSourceItems.get(position).getSourceUrl()));
        holder.mTxtCategory.setText(mSourceItems.get(position).getSourceCategoryName());
        //holder.mImgCategory.setImageResource(mSourceItems.get(position).getSourceCategoryImgId());
        holder.mImgCategory.setImageResource(new Categories(mContext).getDrawableId(mSourceItems.get(position).getSourceCategoryName()));

        //add fading animation as the items start loading
        if (SettingsPreferences.SOURCES_RECYCLER_VIEW_ANIMATION) {
            setAnimation(holder.mItemView, position);
        }

        //set card background color and other things according to dark theme
        if (!SettingsPreferences.THEME) {
            holder.mCardSource.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.darkColorAccent));
            holder.mImgCategory.setColorFilter(ContextCompat.getColor(mContext, R.color.md_grey_300));
        } else {
            //looks ugly
            //holder.mCardSource.setCardBackgroundColor(ContextCompat.getColor(mContext, new ColorsUtil().getRandomColor()));
        }
    }

    private void setAnimation(View view, int position) {
        if (position > mLastPosition) {
            FadeAnimationUtil fadeAnimationUtil = new FadeAnimationUtil(mContext);
            fadeAnimationUtil.fadeInAlpha(view, 500);
            mLastPosition = position;
        }
    }

    //use to remove the sticky animation
    @Override
    public void onViewDetachedFromWindow(SourcesViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.mItemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return mSourceItems.size();
    }

    public int getSourceItemPosition(SourceItem sourceItem) {
        for (int i = 0; i < mSourceItems.size(); i++) {
            if (sourceItem.getSourceName().equals(mSourceItems.get(i).getSourceName())) {
                return i;
            }
        }
        return 0;
    }

    public void removeAt(int position) {
        mSourceItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mSourceItems.size());
    }

    public void modifyAt(int position, SourceItem sourceItem) {
        mSourceItems.set(position, sourceItem);
        notifyItemChanged(position);
    }

    public class SourcesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ISourceView {
        private TextView mTxtSource;
        private TextView mTxtSourceUrl;
        private TextView mTxtCategory;
        private ImageView mImgCategory;
        private CardView mCardSource;
        private View mItemView;

        public SourcesViewHolder(View itemView) {
            super(itemView);
            mTxtSource = (TextView) itemView.findViewById(R.id.text_view_source);
            mTxtSourceUrl = (TextView) itemView.findViewById(R.id.text_view_source_url);
            mTxtCategory = (TextView) itemView.findViewById(R.id.text_view_category);
            mImgCategory = (ImageView) itemView.findViewById(R.id.image_view_category);
            mCardSource = (CardView) itemView.findViewById(R.id.card_view_source);
            mItemView = itemView;
            mCardSource.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mSourcesPresenter == null) {
                mSourcesPresenter = new SourcesPresenter(this, mContext);
            }
            mSourcesPresenter.modifySources(mContext, mSourceItems.get(getAdapterPosition()));
        }

        //No use
        @Override
        public void dataSourceSaved(String message) {

        }

        //No use
        @Override
        public void dataSourceSaveFailed(String message) {

        }

        //No use
        @Override
        public void dataSourceLoaded(List<String> sourceNames) {

        }

        //No use
        @Override
        public void dataSourceItemsLoaded(List<SourceItem> sourceItems) {

        }

        //No use
        @Override
        public void dataSourceLoadingFailed(String message) {

        }

        @Override
        public void sourceItemModified(SourceItem sourceItem, String oldName) {
            SourceItem sourceItemOld = new SourceItem();
            sourceItemOld.setSourceName(oldName);
            modifyAt(getSourceItemPosition(sourceItemOld), sourceItem);
        }

        @Override
        public void sourceItemModificationFailed(String message) {
            Toast.makeText(mContext, "Error:\n" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void sourceItemDeleted(SourceItem sourceItem) {
            removeAt(getSourceItemPosition(sourceItem));
        }

        @Override
        public void sourceItemDeletionFailed(String message) {
            Toast.makeText(mContext, "Error:\n" + message, Toast.LENGTH_SHORT).show();
        }
    }
}
