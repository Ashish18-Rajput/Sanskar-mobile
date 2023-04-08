
package com.sanskar.tv.module.HomeModule.Pojo.bhajanCategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BhajanCat implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("thumbnail1")
    @Expose
    private String thumbnail1;
    @SerializedName("thumbnail2")
    @Expose
    private String thumbnail2;
    @SerializedName("media_file")
    @Expose
    private String mediaFile;
    @SerializedName("artist_name")
    @Expose
    private String artistName;
    @SerializedName("artist_image")
    @Expose
    private String artistImage;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("related_guru")
    @Expose
    private String relatedGuru;
    @SerializedName("artists_id")
    @Expose
    private String artistsId;
    @SerializedName("likes")
    @Expose
    private String likes;
    @SerializedName("creation_time")
    @Expose
    private String creationTime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("direct_play")
    @Expose
    private String directPlay;
    @SerializedName("is_like")
    @Expose
    private String isLike;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumbnail1() {
        return thumbnail1;
    }

    public void setThumbnail1(String thumbnail1) {
        this.thumbnail1 = thumbnail1;
    }

    public String getThumbnail2() {
        return thumbnail2;
    }

    public void setThumbnail2(String thumbnail2) {
        this.thumbnail2 = thumbnail2;
    }

    public String getMediaFile() {
        return mediaFile;
    }

    public void setMediaFile(String mediaFile) {
        this.mediaFile = mediaFile;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistImage() {
        return artistImage;
    }

    public void setArtistImage(String artistImage) {
        this.artistImage = artistImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRelatedGuru() {
        return relatedGuru;
    }

    public void setRelatedGuru(String relatedGuru) {
        this.relatedGuru = relatedGuru;
    }

    public String getArtistsId() {
        return artistsId;
    }

    public void setArtistsId(String artistsId) {
        this.artistsId = artistsId;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDirectPlay() {
        return directPlay;
    }

    public void setDirectPlay(String directPlay) {
        this.directPlay = directPlay;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

}
