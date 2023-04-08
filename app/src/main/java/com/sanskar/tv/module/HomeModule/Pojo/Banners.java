package com.sanskar.tv.module.HomeModule.Pojo;

/**
 * Created by kapil on 15/5/18.
 */

public class Banners {

    private String published_date;

    private String position;

    private String id;

    private String title;

    private String status;

    private String creation_time;

    private String image;

    public String getPublished_date ()
    {
        return published_date;
    }

    public void setPublished_date (String published_date)
    {
        this.published_date = published_date;
    }

    public String getPosition ()
    {
        return position;
    }

    public void setPosition (String position)
    {
        this.position = position;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
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
        return "ClassPojo [published_date = "+published_date+", position = "+position+", id = "+id+", title = "+title+", status = "+status+", creation_time = "+creation_time+", image = "+image+"]";
    }
}
