package com.crazyhitty.chdev.ks.munch.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.munch.R;
import com.crazyhitty.chdev.ks.munch.models.SourceItem;
import com.crazyhitty.chdev.ks.munch.sources.ISourceView;
import com.crazyhitty.chdev.ks.munch.sources.SourcesPresenter;
import com.crazyhitty.chdev.ks.munch.ui.adapters.SourcesRecyclerViewAdapter;
import com.crazyhitty.chdev.ks.munch.utils.ItemDecorationUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kartik_ch on 12/6/2015.
 */
public class ManageSourcesFragment extends Fragment implements ISourceView {
    @Bind(R.id.recycler_view_sources)
    RecyclerView recyclerViewSources;

    private SourcesRecyclerViewAdapter mSourcesRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SourcesPresenter mSourcesPresenter;
    private RecyclerView.ItemDecoration mItemDecoration;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_sources, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mSourcesPresenter == null) {
            mSourcesPresenter = new SourcesPresenter(this, getActivity());
        }
        //mLayoutManager=new LinearLayoutManager(getActivity());
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewSources.setLayoutManager(mLayoutManager);

        mItemDecoration = new ItemDecorationUtil(2, 8, false);
        recyclerViewSources.addItemDecoration(mItemDecoration);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSourcesPresenter.getSourceItems();
            }
        }, 500);
    }

    //no use
    @Override
    public void dataSourceSaved(String message) {

    }

    //no use
    @Override
    public void dataSourceSaveFailed(String message) {

    }

    //no use
    @Override
    public void dataSourceLoaded(List<String> sourceNames) {

    }

    @Override
    public void dataSourceItemsLoaded(List<SourceItem> sourceItems) {
        mSourcesRecyclerViewAdapter = new SourcesRecyclerViewAdapter(getActivity(), sourceItems);
        recyclerViewSources.setAdapter(mSourcesRecyclerViewAdapter);
    }

    @Override
    public void dataSourceLoadingFailed(String message) {
        Toast.makeText(getActivity(), "Error:\n" + message, Toast.LENGTH_SHORT).show();
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
}
