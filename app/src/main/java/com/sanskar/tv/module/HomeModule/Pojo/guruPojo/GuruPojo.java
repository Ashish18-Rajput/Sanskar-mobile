package com.sanskar.tv.module.HomeModule.Pojo.guruPojo;

import java.io.Serializable;

/**
 * Created by Sushant on 2/15/2018.
 */

public class GuruPojo implements Serializable {

    private String likes_count;

    private String id;

    private String is_follow;

    private String status;

    private String description;

    private String profile_image;

    private String is_like;

    private String name;

    private String creation_time;

    private String banner_image;

    private String followers_count;

    public String getLikes_count ()
    {
        return likes_count;
    }

    public void setLikes_count (String likes_count)
    {
        this.likes_count = likes_count;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getIs_follow ()
    {
        return is_follow;
    }

    public void setIs_follow (String is_follow)
    {
        this.is_follow = is_follow;
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

    public String getProfile_image ()
    {
        return profile_image;
    }

    public void setProfile_image (String profile_image)
    {
        this.profile_image = profile_image;
    }

    public String getIs_like ()
    {
        return is_like;
    }

    public void setIs_like (String is_like)
    {
        this.is_like = is_like;
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

    public String getBanner_image ()
    {
        return banner_image;
    }

    public void setBanner_image (String banner_image)
    {
        this.banner_image = banner_image;
    }

    public String getFollowers_count ()
    {
        return followers_count;
    }

    public void setFollowers_count (String followers_count)
    {
        this.followers_count = followers_count;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [likes_count = "+likes_count+", id = "+id+", is_follow = "+is_follow+", status = "+status+", description = "+description+", profile_image = "+profile_image+", is_like = "+is_like+", name = "+name+", creation_time = "+creation_time+", banner_image = "+banner_image+", followers_count = "+followers_count+"]";
    }
}