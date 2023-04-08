package com.sanskar.tv.module.HomeModule.Pojo;

import java.io.File;
import java.io.Serializable;

public class OfflineData implements Serializable {
    public String name;
    public String path;
    public File size;
    public String thumb;
    public String title;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public File getSize() {
        return size;
    }

    public void setSize(File size) {
        this.size = size;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
