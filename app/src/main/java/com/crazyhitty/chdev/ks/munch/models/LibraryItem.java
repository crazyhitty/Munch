package com.crazyhitty.chdev.ks.munch.models;

import android.content.Context;

import com.crazyhitty.chdev.ks.munch.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kartik_ch on 12/20/2015.
 */
public class LibraryItem {
    private String name, creator, desc, link;

    public List<LibraryItem> getLibraryItems(Context context) {
        List<LibraryItem> libraryItems = new ArrayList<>();

        String[] libraryNames = context.getResources().getStringArray(R.array.library_names);
        String[] libraryCreators = context.getResources().getStringArray(R.array.library_creators);
        String[] libraryLinks = context.getResources().getStringArray(R.array.library_links);
        String[] libraryDescs = context.getResources().getStringArray(R.array.library_descs);

        for (int i = 0; i < libraryNames.length; i++) {
            LibraryItem libraryItem = new LibraryItem();
            libraryItem.setName(libraryNames[i]);
            libraryItem.setCreator(libraryCreators[i]);
            libraryItem.setLink(libraryLinks[i]);
            libraryItem.setDesc(libraryDescs[i]);
            libraryItems.add(libraryItem);
        }

        return libraryItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
