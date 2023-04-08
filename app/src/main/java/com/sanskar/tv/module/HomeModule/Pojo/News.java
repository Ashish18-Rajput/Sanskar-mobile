package com.sanskar.tv.module.HomeModule.Pojo;

import java.io.Serializable;

public class News implements Serializable
{
    private String id;

    private String views_count;

    private String title;

    private String status;

    private String description;

    private String creation_time;

    private String image;

    private String shortDesc;

    private String published_date;

    public String getPublished_date() {
        return published_date;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getViews_count ()
    {
        return views_count;
    }

    public void setViews_count (String views_count)
    {
        this.views_count = views_count;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
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

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", views_count = "+views_count+", title = "+title+", status = "+status+", description = "+description+", creation_time = "+creation_time+", shortDesc = "+shortDesc+", image = "+image+"]";
    }
}

			