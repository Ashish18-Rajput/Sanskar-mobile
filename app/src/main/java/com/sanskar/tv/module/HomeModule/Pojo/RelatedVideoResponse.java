package com.sanskar.tv.module.HomeModule.Pojo;

import java.io.Serializable;

public class RelatedVideoResponse implements Serializable
{
    private String tags;

    private String status;

    private String is_like;

    private String author_image;

    private String video_title;

    private String video_desc;

    private String id;

    private String category;

    private String thumbnail_url;

    private String related_guru;

    private String video_url;

    private String views;

    private String likes;

    private String author_name;

    private String creation_time;

    private String comments;

    public String getTags ()
    {
        return tags;
    }

    public void setTags (String tags)
    {
        this.tags = tags;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getIs_like ()
    {
        return is_like;
    }

    public void setIs_like (String is_like)
    {
        this.is_like = is_like;
    }

    public String getAuthor_image ()
    {
        return author_image;
    }

    public void setAuthor_image (String author_image)
    {
        this.author_image = author_image;
    }

    public String getVideo_title ()
    {
        return video_title;
    }

    public void setVideo_title (String video_title)
    {
        this.video_title = video_title;
    }

    public String getVideo_desc ()
    {
        return video_desc;
    }

    public void setVideo_desc (String video_desc)
    {
        this.video_desc = video_desc;
    }

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

    public String getThumbnail_url ()
    {
        return thumbnail_url;
    }

    public void setThumbnail_url (String thumbnail_url)
    {
        this.thumbnail_url = thumbnail_url;
    }

    public String getRelated_guru ()
    {
        return related_guru;
    }

    public void setRelated_guru (String related_guru)
    {
        this.related_guru = related_guru;
    }

    public String getVideo_url ()
    {
        return video_url;
    }

    public void setVideo_url (String video_url)
    {
        this.video_url = video_url;
    }

    public String getViews ()
    {
        return views;
    }

    public void setViews (String views)
    {
        this.views = views;
    }

    public String getLikes ()
    {
        return likes;
    }

    public void setLikes (String likes)
    {
        this.likes = likes;
    }

    public String getAuthor_name ()
    {
        return author_name;
    }

    public void setAuthor_name (String author_name)
    {
        this.author_name = author_name;
    }

    public String getCreation_time ()
    {
        return creation_time;
    }

    public void setCreation_time (String creation_time)
    {
        this.creation_time = creation_time;
    }

    public String getComments ()
    {
        return comments;
    }

    public void setComments (String comments)
    {
        this.comments = comments;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [tags = "+tags+", status = "+status+", is_like = "+is_like+", author_image = "+author_image+", video_title = "+video_title+", video_desc = "+video_desc+", id = "+id+", category = "+category+", thumbnail_url = "+thumbnail_url+", related_guru = "+related_guru+", video_url = "+video_url+", views = "+views+", likes = "+likes+", author_name = "+author_name+", creation_time = "+creation_time+", comments = "+comments+"]";
    }
}