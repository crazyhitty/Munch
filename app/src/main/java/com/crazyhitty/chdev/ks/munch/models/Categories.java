package com.crazyhitty.chdev.ks.munch.models;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;

import com.crazyhitty.chdev.ks.munch.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kartik_ch on 12/13/2015.
 */
public class Categories {
    private Context mContext;

    public Categories(Context context) {
        this.mContext = context;
    }

    public List<CategoryItem> getCategoryItems() {
        List<CategoryItem> categoryItems = new ArrayList<>();

        String[] categoryNames = mContext.getResources().getStringArray(R.array.category_names);
        TypedArray categoryImgs = mContext.getResources().obtainTypedArray(R.array.category_drawables);

        for (int i = 0; i < categoryNames.length; i++) {
            CategoryItem categoryItem = new CategoryItem();
            categoryItem.setCategoryName(categoryNames[i]);
            categoryItem.setCategoryImg(ContextCompat.getDrawable(mContext, categoryImgs.getResourceId(i, -1)));
            categoryItems.add(categoryItem);
        }

        return categoryItems;
    }

    public int getDrawableId(String category) {
        switch (category) {
            case "Automobiles":
                return R.drawable.ic_automobile_24dp;
            case "Books":
                return R.drawable.ic_book_24dp;
            case "Business":
                return R.drawable.ic_business_24dp;
            case "Climate":
                return R.drawable.ic_climate_24dp;
            case "Comics":
                return R.drawable.ic_comics_24dp;
            case "Deals":
                return R.drawable.ic_deals_24dp;
            case "Economics":
                return R.drawable.ic_economics_24dp;
            case "Education":
                return R.drawable.ic_education_24dp;
            case "Entertainment":
                return R.drawable.ic_entertainment_24dp;
            case "Fashion":
                return R.drawable.ic_fashion_24dp;
            case "Finance":
                return R.drawable.ic_finance_24dp;
            case "Fitness":
                return R.drawable.ic_fitness_24dp;
            case "Food":
                return R.drawable.ic_food_24dp;
            case "Gadgets":
                return R.drawable.ic_gadgets_24dp;
            case "Gaming":
                return R.drawable.ic_gaming_24dp;
            case "Hardware":
                return R.drawable.ic_hardware_24dp;
            case "Health":
                return R.drawable.ic_health_24dp;
            case "International":
                return R.drawable.ic_international_24dp;
            case "Marketing":
                return R.drawable.ic_marketing_24dp;
            case "Music":
                return R.drawable.ic_music_24dp;
            case "National":
                return R.drawable.ic_national_24dp;
            case "Politics":
                return R.drawable.ic_politics_24dp;
            case "Productivity":
                return R.drawable.ic_productivity_24dp;
            case "Programming":
                return R.drawable.ic_programming_24dp;
            case "Research":
                return R.drawable.ic_research_24dp;
            case "Science":
                return R.drawable.ic_science_24dp;
            case "Software":
                return R.drawable.ic_software_24dp;
            case "Space":
                return R.drawable.ic_space_24dp;
            case "Sports":
                return R.drawable.ic_sports_24dp;
            case "Technology":
                return R.drawable.ic_technology_24dp;
            case "Unknown":
                return R.drawable.ic_unknown_24dp;
            default:
                return 0;
        }
    }
}
