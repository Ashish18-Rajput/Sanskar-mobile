package com.sanskar.tv.module.HomeModule.Pojo;

import java.io.Serializable;

/**
 * Created by appsquadz on 2/15/2018.
 */

public class VideoResponse2 implements Serializable {
    private String category_name;

    private Videos[] videos;

    public String getCategory ()
    {
        return category_name;
    }

    public void setCategory (String category_name)
    {
        this.category_name = category_name;
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
        return "ClassPojo [category_name = "+category_name+", videos = "+videos+"]";
    }
}