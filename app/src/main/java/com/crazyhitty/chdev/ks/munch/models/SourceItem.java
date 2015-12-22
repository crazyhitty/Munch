package com.crazyhitty.chdev.ks.munch.models;

/**
 * Created by Kartik_ch on 11/7/2015.
 */
public class SourceItem {
    private String sourceName, sourceUrl, sourceCategoryName, sourceDateAdded;
    private int sourceCategoryImgId;

    public int getSourceCategoryImgId() {
        return sourceCategoryImgId;
    }

    public void setSourceCategoryImgId(int sourceCategoryImgId) {
        this.sourceCategoryImgId = sourceCategoryImgId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceDateAdded() {
        return sourceDateAdded;
    }

    public void setSourceDateAdded(String sourceDateAdded) {
        this.sourceDateAdded = sourceDateAdded;
    }

    public String getSourceCategoryName() {
        return sourceCategoryName;
    }

    public void setSourceCategoryName(String sourceCategoryName) {
        this.sourceCategoryName = sourceCategoryName;
    }
}
