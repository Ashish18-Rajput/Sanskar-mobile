package com.sanskar.tv.module.HomeModule.Pojo;

import java.io.Serializable;

public class WallPaperModel implements Serializable {
    String title;
    String wallpaper;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWallpaper() {
        return wallpaper;
    }

    public void setWallpaper(String wallpaper) {
        this.wallpaper = wallpaper;
    }
}
