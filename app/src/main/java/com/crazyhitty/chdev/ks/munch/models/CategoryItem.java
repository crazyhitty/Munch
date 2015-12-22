package com.crazyhitty.chdev.ks.munch.models;

import android.graphics.drawable.Drawable;

/**
 * Created by Kartik_ch on 11/11/2015.
 */
public class CategoryItem {
    private String categoryName;
    private Drawable categoryImg;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Drawable getCategoryImg() {
        return categoryImg;
    }

    public void setCategoryImg(Drawable categoryImg) {
        this.categoryImg = categoryImg;
    }
}
