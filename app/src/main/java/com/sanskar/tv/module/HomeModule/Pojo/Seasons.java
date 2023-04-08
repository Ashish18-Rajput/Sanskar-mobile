
package com.sanskar.tv.module.HomeModule.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Seasons {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("season_title")
    @Expose
    private String seasonTitle;
    @SerializedName("promo_video")
    @Expose
    private String promoVideo;
    @SerializedName("yt_promo_video")
    @Expose
    private String ytPromoVideo;
    @SerializedName("free_video")
    @Expose
    private String freeVideo;
    @SerializedName("yt_free_video")
    @Expose
    private String ytFreeVideo;
    @SerializedName("category_ids")
    @Expose
    private String categoryIds;
    @SerializedName("author_id")
    @Expose
    private String authorId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("published_date")
    @Expose
    private String publishedDate;
    @SerializedName("position")
    @Expose
    private String position;
    @SerializedName("author_name")
    @Expose
    private String authorName;
    @SerializedName("categories")
    @Expose
    private String categories;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeasonTitle() {
        return seasonTitle;
    }

    public void setSeasonTitle(String seasonTitle) {
        this.seasonTitle = seasonTitle;
    }

    public String getPromoVideo() {
        return promoVideo;
    }

    public void setPromoVideo(String promoVideo) {
        this.promoVideo = promoVideo;
    }

    public String getYtPromoVideo() {
        return ytPromoVideo;
    }

    public void setYtPromoVideo(String ytPromoVideo) {
        this.ytPromoVideo = ytPromoVideo;
    }

    public String getFreeVideo() {
        return freeVideo;
    }

    public void setFreeVideo(String freeVideo) {
        this.freeVideo = freeVideo;
    }

    public String getYtFreeVideo() {
        return ytFreeVideo;
    }

    public void setYtFreeVideo(String ytFreeVideo) {
        this.ytFreeVideo = ytFreeVideo;
    }

    public String getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(String categoryIds) {
        this.categoryIds = categoryIds;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

}
