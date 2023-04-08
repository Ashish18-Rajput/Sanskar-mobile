package com.sanskar.tv.module.HomeModule.Pojo;

/**
 * Created by kapil on 30/4/18.
 */

public class AudioBean {

    private String id;

    private String category;

    private String title;

    private String related_guru;

    private String status;

    private String artist_name;

    private String artist_image;

    private String description;

    private String likes;

    private String media_file;

    private String creation_time;

    private String image;

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

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getRelated_guru ()
    {
        return related_guru;
    }

    public void setRelated_guru (String related_guru)
    {
        this.related_guru = related_guru;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getArtist_name ()
    {
        return artist_name;
    }

    public void setArtist_name (String artist_name)
    {
        this.artist_name = artist_name;
    }

    public String getArtist_image ()
    {
        return artist_image;
    }

    public void setArtist_image (String artist_image)
    {
        this.artist_image = artist_image;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getLikes ()
    {
        return likes;
    }

    public void setLikes (String likes)
    {
        this.likes = likes;
    }

    public String getMedia_file ()
    {
        return media_file;
    }

    public void setMedia_file (String media_file)
    {
        this.media_file = media_file;
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
        return "ClassPojo [id = "+id+", category = "+category+", title = "+title+", related_guru = "+related_guru+", status = "+status+", artist_name = "+artist_name+", artist_image = "+artist_image+", description = "+description+", likes = "+likes+", media_file = "+media_file+", creation_time = "+creation_time+", image = "+image+"]";
    }
}


