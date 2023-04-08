package com.sanskar.tv.module.HomeModule.Pojo;

import java.io.Serializable;

public class Comments implements Serializable
{
    private String id;

    private String profile_picture;

    private String time;

    private String name;

    private String user_id;

    private String video_id;

    private String comment;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getProfile_picture ()
    {
        return profile_picture;
    }

    public void setProfile_picture (String profile_picture)
    {
        this.profile_picture = profile_picture;
    }

    public String getTime ()
    {
        return time;
    }

    public void setTime (String time)
    {
        this.time = time;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    public String getVideo_id ()
    {
        return video_id;
    }

    public void setVideo_id (String video_id)
    {
        this.video_id = video_id;
    }

    public String getComment ()
    {
        return comment;
    }

    public void setComment (String comment)
    {
        this.comment = comment;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", profile_picture = "+profile_picture+", time = "+time+", name = "+name+", user_id = "+user_id+", video_id = "+video_id+", comment = "+comment+"]";
    }
}
