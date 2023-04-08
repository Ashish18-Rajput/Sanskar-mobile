package com.sanskar.tv.module.HomeModule.Pojo;

import java.io.Serializable;

public class VideoResponse implements Serializable
{
    private String id;

    private String category;

    private Videos[] videos = new Videos[]{};

    public String getCategory ()
    {
        return category;
    }

    public void setCategory (String category)
    {
        this.category = category;
    }

    public Videos[] getVideos ()
    {
        return videos;
    }

    public void setVideos (Videos[] videos)
    {
        this.videos = videos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = " +id+ "category = "+category+", videos = "+videos+"]";
    }
}