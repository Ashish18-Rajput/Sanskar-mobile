
package com.sanskar.tv.module.HomeModule.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PremiumCategory {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("season_thumbnails")
    @Expose
    private List<String> seasonThumbnails = null;
    @SerializedName("season_short_videos")
    @Expose
    private List<String> seasonShortVideos = null;
    @SerializedName("season_promo_videos")
    @Expose
    private List<MenuMasterList> season_promo_videos = null;

    public List<MenuMasterList> getSeason_promo_videos() {
        return season_promo_videos;
    }

    public void setSeason_promo_videos(List<MenuMasterList> season_promo_videos) {
        this.season_promo_videos = season_promo_videos;
    }

    @SerializedName("data")
    @Expose
    private List<MenuMasterList> data = null;
    @SerializedName("error")
    @Expose
    private List<Object> error = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public PremiumCategory() {
    }

    /**
     * 
     * @param data
     * @param message
     * @param seasonThumbnails
     * @param seasonShortVideos
     * @param error
     * @param status
     */
    public PremiumCategory(Boolean status, String message, List<String> seasonThumbnails, List<String> seasonShortVideos, List<MenuMasterList> data, List<Object> error) {
        super();
        this.status = status;
        this.message = message;
        this.seasonThumbnails = seasonThumbnails;
        this.seasonShortVideos = seasonShortVideos;
        this.data = data;
        this.error = error;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getSeasonThumbnails() {
        return seasonThumbnails;
    }

    public void setSeasonThumbnails(List<String> seasonThumbnails) {
        this.seasonThumbnails = seasonThumbnails;
    }

    public List<String> getSeasonShortVideos() {
        return seasonShortVideos;
    }

    public void setSeasonShortVideos(List<String> seasonShortVideos) {
        this.seasonShortVideos = seasonShortVideos;
    }

    public List<MenuMasterList> getData() {
        return data;
    }

    public void setData(List<MenuMasterList> data) {
        this.data = data;
    }

    public List<Object> getError() {
        return error;
    }

    public void setError(List<Object> error) {
        this.error = error;
    }

}
