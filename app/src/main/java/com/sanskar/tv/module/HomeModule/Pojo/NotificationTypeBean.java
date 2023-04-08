package com.sanskar.tv.module.HomeModule.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kapil on 10/5/18.
 */

public class NotificationTypeBean implements Serializable/*{

    private String id;

    private String description;

    private String image;

    private String creation_time;

    private String post_type;

    private String user_id;

    private String post_id;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }

    public String getCreation_time ()
    {
        return creation_time;
    }

    public void setCreation_time (String creation_time)
    {
        this.creation_time = creation_time;
    }

    public String getPost_type ()
    {
        return post_type;
    }

    public void setPost_type (String post_type)
    {
        this.post_type = post_type;
    }

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    public String getPost_id ()
    {
        return post_id;
    }

    public void setPost_id (String post_id)
    {
        this.post_id = post_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", description = "+description+", image = "+image+", creation_time = "+creation_time+", post_type = "+post_type+", user_id = "+user_id+", post_id = "+post_id+"]";
    }*/

{

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_type")
        @Expose
        private String userType;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("post_type")
        @Expose
        private String postType;
        @SerializedName("post_id")
        @Expose
        private String postId;
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("device_type")
        @Expose
        private String deviceType;
        @SerializedName("notification_type")
        @Expose
        private String notificationType;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("extra")
        @Expose
        private String extra;
        @SerializedName("sent_by")
        @Expose
        private String sentBy;
        @SerializedName("created_on")
        @Expose
        private String createdOn;

    @SerializedName("notification_thumbnail")
    @Expose
    private String notification_thumbnail;

    public boolean isSetcheckbox() {
        return setcheckbox;
    }

    public void setSetcheckbox(boolean setcheckbox) {
        this.setcheckbox = setcheckbox;
    }

    private  boolean setcheckbox =false;
    private  boolean MarkCheckBox =false;
    private  boolean UnMarkCheckBox =false;

    public boolean isMarkCheckBox() {
        return MarkCheckBox;
    }

    public void setMarkCheckBox(boolean markCheckBox) {
        MarkCheckBox = markCheckBox;
    }

    public boolean isUnMarkCheckBox() {
        return UnMarkCheckBox;
    }

    public void setUnMarkCheckBox(boolean unMarkCheckBox) {
        UnMarkCheckBox = unMarkCheckBox;
    }

    @SerializedName("is_view")
    @Expose
    private String isView;

    public String getNotification_thumbnail() {
        return notification_thumbnail;
    }

    public void setNotification_thumbnail(String notification_thumbnail) {
        this.notification_thumbnail = notification_thumbnail;
    }

    private boolean isSelected;
    private  boolean isvisible=false;

    public boolean isIsvisible() {
        return isvisible;
    }

    public void setIsvisible(boolean isvisible) {
        this.isvisible = isvisible;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    /* private  String is_selected;

        public String getIs_selected() {
            return is_selected;
        }

        public void setIs_selected(String is_selected) {
            this.is_selected = is_selected;
        }
    */
    public String getIsView() {
        return isView;
    }

    public void setIsView(String isView) {
        this.isView = isView;
    }

    public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPostType() {
            return postType;
        }

        public void setPostType(String postType) {
            this.postType = postType;
        }

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getNotificationType() {
            return notificationType;
        }

        public void setNotificationType(String notificationType) {
            this.notificationType = notificationType;
        }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }

        public String getSentBy() {
            return sentBy;
        }

        public void setSentBy(String sentBy) {
            this.sentBy = sentBy;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }


}
