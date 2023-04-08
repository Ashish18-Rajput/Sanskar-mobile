
package com.sanskar.tv.module.HomeModule.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeasonDetail {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("season_title")
    @Expose
    private String seasonTitle;
    @SerializedName("season_thumbnail")
    @Expose
    private String seasonThumbnail;
    @SerializedName("promo_video")
    @Expose
    private String promoVideo;
    @SerializedName("yt_promo_video")
    @Expose
    private String ytPromoVideo;
    @SerializedName("author_id")
    @Expose
    private String authorId;
    @SerializedName("author_name")
    @Expose
    private String authorName;

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

    public String getSeasonThumbnail() {
        return seasonThumbnail;
    }

    public void setSeasonThumbnail(String seasonThumbnail) {
        this.seasonThumbnail = seasonThumbnail;
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

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

}
