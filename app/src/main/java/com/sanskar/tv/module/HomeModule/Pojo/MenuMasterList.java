
package com.sanskar.tv.module.HomeModule.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MenuMasterList implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("youtube")
    @Expose
    private String youtube;
    @SerializedName("normal")
    @Expose
    private String normal;

    private boolean is_now_playing = false;

    public boolean isIs_now_playing() {
        return is_now_playing;
    }

    public void setIs_now_playing(boolean is_now_playing) {
        this.is_now_playing = is_now_playing;
    }

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("encrypted_url")
    @Expose
    private String encrypted_url;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEncrypted_url() {
        return encrypted_url;
    }

    public void setEncrypted_url(String encrypted_url) {
        this.encrypted_url = encrypted_url;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("channel_url")
    @Expose
    private String channelUrl;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("likes")
    @Expose
    private String likes;



    @SerializedName("live_users")
    @Expose
    private String liveUsers;
    @SerializedName("creation_time")
    @Expose
    private String creationTime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("banner_image")
    @Expose
    private String bannerImage;
    @SerializedName("followers_count")
    @Expose
    private String followersCount;
    @SerializedName("likes_count")
    @Expose
    private String likesCount;

    @SerializedName("video_title")
    @Expose
    private String videoTitle;

    @SerializedName("custom_promo_video")
    @Expose
    private String custom_promo_video;

    @SerializedName("custom_episode_url")
    @Expose
    private String custom_episode_url;

    public String getCustom_episode_url() {
        return custom_episode_url;
    }

    public void setCustom_episode_url(String custom_episode_url) {
        this.custom_episode_url = custom_episode_url;
    }

    public String getCustom_promo_video() {
        return custom_promo_video;
    }

    public void setCustom_promo_video(String custom_promo_video) {
        this.custom_promo_video = custom_promo_video;
    }

    @SerializedName("youtube_views")
    @Expose
    private String youtube_views;

    public String getYoutube_views() {
        return youtube_views;
    }

    public void setYoutube_views(String youtube_views) {
        this.youtube_views = youtube_views;
    }

    @SerializedName("pause_at")
    @Expose
    private String pause_at;

    @SerializedName("video_url")
    @Expose
    private String videoUrl;
    @SerializedName("type")
    @Expose
    private String type;

    public String getPause_at() {
        return pause_at;
    }

    public void setPause_at(String pause_at) {
        this.pause_at = pause_at;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("author_name")
    @Expose
    private String authorName;


    @SerializedName("total_play")
    @Expose
    private String totalPlay;

    public String getTotalPlay() {
        return totalPlay;
    }

    public void setTotalPlay(String totalPlay) {
        this.totalPlay = totalPlay;
    }

    @SerializedName("thumbnail_url")
    @Expose
    private String thumbnailUrl;
    @SerializedName("thumbnail_url1")
    @Expose
    private String thumbnailUrl1;
    @SerializedName("video_desc")
    @Expose
    private String videoDesc;

    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("is_locked")
    @Expose
    private String is_locked;

    public String getIs_locked() {
        return is_locked;
    }

    public void setIs_locked(String is_locked) {
        this.is_locked = is_locked;
    }

    @SerializedName("is_sankirtan")
    @Expose
    private String isSankirtan;
    @SerializedName("is_popular")
    @Expose
    private String isPopular;
    @SerializedName("related_guru")
    @Expose
    private String relatedGuru;
    @SerializedName("author_image")
    @Expose
    private String authorImage;


    @SerializedName("is_audio_playlist_exist")
    @Expose
    private String is_audio_playlist_exist;

    public String getIs_audio_playlist_exist() {
        return is_audio_playlist_exist;
    }

    public void setIs_audio_playlist_exist(String is_audio_playlist_exist) {
        this.is_audio_playlist_exist = is_audio_playlist_exist;
    }

    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("views")
    @Expose
    private String views;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("published_date")
    @Expose
    private String publishedDate;
    @SerializedName("youtube_url")
    @Expose
    private String youtubeUrl;
    @SerializedName("uploaded_by")
    @Expose
    private String uploadedBy;
    @SerializedName("thumbnail1")
    @Expose
    private String thumbnail1;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

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
    @SerializedName("artists_id")
    @Expose
    private String artistsId;
    @SerializedName("episode_description")
    @Expose
    private String episode_description;

    public String getEpisode_description() {
        return episode_description;
    }

    public void setEpisode_description(String episode_description) {
        this.episode_description = episode_description;
    }

    @SerializedName("god_id")
    @Expose
    private String godId;
    @SerializedName("god_name")
    @Expose
    private String godName;
    @SerializedName("god_image")
    @Expose
    private String godImage;
    @SerializedName("direct_play")
    @Expose
    private String directPlay;
    @SerializedName("shortDesc")
    @Expose
    private String shortDesc;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("views_count")
    @Expose
    private String viewsCount;

    @SerializedName("season_id")
    @Expose
    private String season_id;
    @SerializedName("season_title")
    @Expose
    private String season_title;
    @SerializedName("season_thumbnail")
    @Expose
    private String season_thumbnail;
    @SerializedName("promo_video")
    @Expose
    private String promo_video;
    @SerializedName("yt_promo_video")
    @Expose
    private String yt_promo_video;
    @SerializedName("episode_url")
    @Expose
    private String episode_url;

    @SerializedName("yt_episode_url")
    @Expose
    private String yt_episode_url;
    @SerializedName("yt_short_video")
    @Expose
    private String yt_short_video;

    public String getYt_short_video() {
        return yt_short_video;
    }

    public void setYt_short_video(String yt_short_video) {
        this.yt_short_video = yt_short_video;
    }

    @SerializedName("short_video")
    @Expose
    private String short_video;
    @SerializedName("cat_name")
    @Expose
    private String cat_name;

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    @SerializedName("episode_details")
    @Expose
    private List<MenuMasterList> episode_details=null;


    @SerializedName("season_details")
    @Expose
    private List<MenuMasterList> season_details=null;

    public List<MenuMasterList> getSeason_details() {
        return season_details;
    }

    public void setSeason_details(List<MenuMasterList> season_details) {
        this.season_details = season_details;
    }

    public List<MenuMasterList> getEpisode_details() {
        return episode_details;
    }

    public void setEpisode_details(List<MenuMasterList> episode_details) {
        this.episode_details = episode_details;
    }

    public String getShort_video() {
        return short_video;
    }

    public void setShort_video(String short_video) {
        this.short_video = short_video;
    }

    @SerializedName("episode_title")
    @Expose
    private String episode_title;
    @SerializedName("episode_id")
    @Expose
    private String episode_id;
    @SerializedName("is_likes")
    @Expose
    private String is_likes;
    @SerializedName("is_like")
    @Expose
    private String is_like;

    public String getIs_like() {
        return is_like;
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }

    @SerializedName("is_follow")
    @Expose
    private String is_follow;




    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }

    public String getIs_likes() {
        return is_likes;
    }

    public void setIs_likes(String is_likes) {
        this.is_likes = is_likes;
    }

    public String getPromo_video() {
        return promo_video;
    }

    public void setPromo_video(String promo_video) {
        this.promo_video = promo_video;
    }

    public String getYt_promo_video() {
        return yt_promo_video;
    }

    public void setYt_promo_video(String yt_promo_video) {
        this.yt_promo_video = yt_promo_video;
    }

    public String getEpisode_url() {
        return episode_url;
    }

    public void setEpisode_url(String episode_url) {
        this.episode_url = episode_url;
    }

    public String getYt_episode_url() {
        return yt_episode_url;
    }

    public void setYt_episode_url(String yt_episode_url) {
        this.yt_episode_url = yt_episode_url;
    }

    public String getEpisode_title() {
        return episode_title;
    }

    public void setEpisode_title(String episode_title) {
        this.episode_title = episode_title;
    }

    public String getEpisode_id() {
        return episode_id;
    }

    public void setEpisode_id(String episode_id) {
        this.episode_id = episode_id;
    }

    public String getSeason_id() {
        return season_id;
    }

    public void setSeason_id(String season_id) {
        this.season_id = season_id;
    }

    public String getSeason_title() {
        return season_title;
    }

    public void setSeason_title(String season_title) {
        this.season_title = season_title;
    }

    public String getSeason_thumbnail() {
        return season_thumbnail;
    }

    public void setSeason_thumbnail(String season_thumbnail) {
        this.season_thumbnail = season_thumbnail;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(String viewsCount) {
        this.viewsCount = viewsCount;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }


    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getLiveUsers() {
        return liveUsers;
    }

    public void setLiveUsers(String liveUsers) {
        this.liveUsers = liveUsers;
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(String followersCount) {
        this.followersCount = followersCount;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getThumbnailUrl1() {
        return thumbnailUrl1;
    }

    public void setThumbnailUrl1(String thumbnailUrl1) {
        this.thumbnailUrl1 = thumbnailUrl1;
    }

    public String getVideoDesc() {
        return videoDesc;
    }

    public void setVideoDesc(String videoDesc) {
        this.videoDesc = videoDesc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIsSankirtan() {
        return isSankirtan;
    }

    public void setIsSankirtan(String isSankirtan) {
        this.isSankirtan = isSankirtan;
    }

    public String getIsPopular() {
        return isPopular;
    }

    public void setIsPopular(String isPopular) {
        this.isPopular = isPopular;
    }

    public String getRelatedGuru() {
        return relatedGuru;
    }

    public void setRelatedGuru(String relatedGuru) {
        this.relatedGuru = relatedGuru;
    }

    public String getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
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

    public String getArtistsId() {
        return artistsId;
    }

    public void setArtistsId(String artistsId) {
        this.artistsId = artistsId;
    }

    public String getGodId() {
        return godId;
    }

    public void setGodId(String godId) {
        this.godId = godId;
    }

    public String getGodName() {
        return godName;
    }

    public void setGodName(String godName) {
        this.godName = godName;
    }

    public String getGodImage() {
        return godImage;
    }

    public void setGodImage(String godImage) {
        this.godImage = godImage;
    }

    public String getDirectPlay() {
        return directPlay;
    }

    public void setDirectPlay(String directPlay) {
        this.directPlay = directPlay;
    }

}
