package com.sanskar.tv.model;

import java.io.Serializable;

public class VideoPlaylistModel implements Serializable {
    public int _id;
    public String _name;
    public String _url;
    public String img_url;

    public VideoPlaylistModel(String _name, String _url, String img_url) {
        this._name = _name;
        this._url = _url;
        this.img_url = img_url;
    }

    public VideoPlaylistModel(int _id, String _name, String _url) {
        this._id = _id;
        this._name = _name;
        this._url = _url;
        this.img_url = img_url;
    }
    public VideoPlaylistModel() {

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

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;

    }

    @Override
    public String toString()
    {
        return "ClassPojo [ _name = "+_name+", _url = "+_url+", img_url = "+img_url+" ]";
    }
}
