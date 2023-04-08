package com.sanskar.tv.module.HomeModule.Pojo;

import java.io.Serializable;

public class Channel implements Serializable {
    static Channel channelprofile;
    private String id;

    private String status;

    private String name;

    private String creation_time;

    private String image;

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    private String likes;
    private String title;
    private String channel_url;

    public String getIs_likes() {
        return is_likes;
    }

    public void setIs_likes(String is_likes) {
        this.is_likes = is_likes;
    }

    private String is_likes;

    public String getChannel_url() {
        return channel_url;
    }

    public void setChannel_url(String channel_url) {
        this.channel_url = channel_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getCreation_time ()
    {
        return creation_time;
    }

    public void setCreation_time (String creation_time)
    {
        this.creation_time = creation_time;
    }

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }

    public static Channel copyInstance(Channel channel) {
        channelprofile = channel;
        return channelprofile;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", status = "+status+", name = "+name+", creation_time = "+creation_time+", image = "+image+"]";
    }

}
