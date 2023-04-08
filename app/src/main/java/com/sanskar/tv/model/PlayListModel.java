package com.sanskar.tv.model;

import java.io.Serializable;

public class PlayListModel implements Serializable {
    public int _id;
    public String _name;
    public String _url;
    public String img_url;

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

    private String thumbnail1;
    private String thumbnail2;

    public String getThumbnail1() {
        return thumbnail1;
    }

    public void setThumbnail1(String thumbnail1) {
        this.thumbnail1 = thumbnail1;
    }

    public String getThumbnail2() {
        return thumbnail2;
    }

    public void setThumbnail2(String thumbnail2) {
        this.thumbnail2 = thumbnail2;
    }

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

    public PlayListModel(String _name, String _url,String img_url) {
        this._name = _name;
        this._url = _url;
        this.img_url = img_url;
    }

    public PlayListModel(int _id, String _name, String _url) {
        this._id = _id;
        this._name = _name;
        this._url = _url;
    }

    public PlayListModel() {

    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_url() {
        return _url;
    }

    public void set_url(String _url) {
        this._url = _url;
    }
}
