package com.sanskar.tv.module.HomeModule.Pojo;

/**
 * Created by kapil on 1/5/18.
 */

public class SankirtanBean {

    private String id;

    private String category;

    private Videos[] videos;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

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

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", category = "+category+", videos = "+videos+"]";
    }
}
