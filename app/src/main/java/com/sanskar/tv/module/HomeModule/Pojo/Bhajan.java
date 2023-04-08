package com.sanskar.tv.module.HomeModule.Pojo;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Bhajan implements Serializable
{
    private String artist_id;

    private String status;

    private String is_like;

    private String image;

    private String direct_play;

    private String id;

    private String title;

    private String category;

    private String related_guru;

    private String artist_name;

    private String description;

    private String artist_image;

    private String media_file;
    private String god_id;
    private String god_name;
    private String god_image;

    public String getGod_id() {
        return god_id;
    }

    public void setGod_id(String god_id) {
        this.god_id = god_id;
    }

    public String getGod_name() {
        return god_name;
    }

    public void setGod_name(String god_name) {
        this.god_name = god_name;
    }

    public String getGod_image() {
        return god_image;
    }

    public void setGod_image(String god_image) {
        this.god_image = god_image;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    private String likes;

    private String creation_time;

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

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }

    public String getDirect_play ()
    {
        return direct_play;
    }

    public void setDirect_play (String direct_play)
    {
        this.direct_play = direct_play;
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

    public String getCategory ()
    {
        return category;
    }

    public void setCategory (String category)
    {
        this.category = category;
    }

    public String getRelated_guru ()
    {
        return related_guru;
    }

    public void setRelated_guru (String related_guru)
    {
        this.related_guru = related_guru;
    }

    public String getArtist_name ()
    {
        return artist_name;
    }

    public void setArtist_name (String artist_name)
    {
        this.artist_name = artist_name;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getArtist_image() {
        return artist_image;
    }

    public void setArtist_image(String artist_image) {
        this.artist_image = artist_image;
    }

    public String getMedia_file ()
    {
        return media_file;
    }

    public void setMedia_file (String media_file)
    {
        this.media_file = media_file;
    }

    public String getLikes ()
    {
        return likes;
    }

    public void setLikes (String likes)
    {
        this.likes = likes;
    }

    public String getCreation_time ()
    {
        return creation_time;
    }

    public void setCreation_time (String creation_time)
    {
        this.creation_time = creation_time;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [status = "+status+", is_like = "+is_like+", image = "+image+", direct_play = "+direct_play+", id = "+id+", title = "+title+", category = "+category+", related_guru = "+related_guru+", artist_name = "+artist_name+", description = "+description+", artist_image = "+artist_image+", media_file = "+media_file+", likes = "+likes+", artist_id = "+artist_id+", creation_time = "+creation_time+"]";
    }

    @Override
    public int hashCode() {
        return (this.title.hashCode() + this.artist_id.hashCode() + this.artist_image.hashCode() + this.description.hashCode() + this.is_like.hashCode() + this.category.hashCode() + this.status.hashCode() +
                this.direct_play.hashCode() + this.id.hashCode() + this.god_id.hashCode() + this.god_image.hashCode() + this.god_name.hashCode() + this.image.hashCode() + this.related_guru.hashCode() +
                this.media_file.hashCode() + this.artist_name.hashCode());
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Bhajan){
            Bhajan temp=(Bhajan)obj;
            if(this.title == temp.title && this.artist_id== temp.artist_id && this.artist_image == temp.artist_image && this.description == temp.description && this.is_like==temp.is_like && this.category==temp.category && this.status==temp.status
            && this.direct_play==temp.direct_play &&this.id==temp.id && this.god_id==temp.god_id && this.god_image==temp.god_image && this.god_name==temp.god_name
            && this.image==temp.image && this.related_guru==temp.related_guru && this.media_file==temp.media_file && this.artist_name==temp.artist_name )
                return true;
        }
        return false;
    }
}
