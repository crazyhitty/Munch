package com.crazyhitty.chdev.ks.munch.models;

/**
 * Created by Kartik_ch on 11/5/2015.
 */
public class FeedItem {
    private String itemTitle, itemDesc, itemSourceUrl, itemLink, itemImgUrl, itemCategory, itemSource, itemPubDate, itemWebDesc, itemWebDescSync;
    private int itemCategoryImgId, itemBgId;

    public String getItemTitle() {
        if (itemTitle == null) {
            return "";
        }
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemDesc() {
        if (itemDesc == null) {
            return "";
        }
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemSourceUrl() {
        if (itemSourceUrl == null) {
            return "";
        }
        return itemSourceUrl;
    }

    public void setItemSourceUrl(String itemSourceUrl) {
        this.itemSourceUrl = itemSourceUrl;
    }

    public String getItemLink() {
        if (itemLink == null) {
            return "";
        }
        return itemLink;
    }

    public void setItemLink(String itemLink) {
        this.itemLink = itemLink;
    }

    public String getItemImgUrl() {
        if (itemImgUrl == null) {
            return "";
        }
        return itemImgUrl;
    }

    public void setItemImgUrl(String itemImgUrl) {
        this.itemImgUrl = itemImgUrl;
    }

    public String getItemCategory() {
        if (itemCategory == null) {
            return "";
        }
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemSource() {
        if (itemSource == null) {
            return "";
        }
        return itemSource;
    }

    public void setItemSource(String itemSource) {
        this.itemSource = itemSource;
    }

    public String getItemPubDate() {
        if (itemPubDate == null) {
            return "";
        }
        return itemPubDate;
    }

    public void setItemPubDate(String itemPubDate) {
        this.itemPubDate = itemPubDate;
    }

    public int getItemCategoryImgId() {
        return itemCategoryImgId;
    }

    public void setItemCategoryImgId(int itemCategoryImgId) {
        this.itemCategoryImgId = itemCategoryImgId;
    }

    public int getItemBgId() {
        return itemBgId;
    }

    public void setItemBgId(int itemBgId) {
        this.itemBgId = itemBgId;
    }

    public String getItemWebDesc() {
        return itemWebDesc;
    }

    public void setItemWebDesc(String itemWebDesc) {
        this.itemWebDesc = itemWebDesc;
    }

    public String getItemWebDescSync() {
        return itemWebDescSync;
    }

    public void setItemWebDescSync(String itemWebDescSync) {
        this.itemWebDescSync = itemWebDescSync;
    }
}
