package com.crazyhitty.chdev.ks.rssmanager;

/**
 * Created by Kartik_ch on 11/15/2015.
 */
public class RssItem {
    String title;
    String description;
    String link;
    String sourceName;
    String sourceUrl;
    String imageUrl;
    String category;
    String pubDate;
    int categoryImgId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public int getCategoryImgId() {
        return categoryImgId;
    }

    public void setCategoryImgId(int categoryImgId) {
        this.categoryImgId = categoryImgId;
    }
}
