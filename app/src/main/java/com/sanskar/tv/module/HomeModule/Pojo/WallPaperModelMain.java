package com.sanskar.tv.module.HomeModule.Pojo;

import java.util.List;

public class WallPaperModelMain {
    String id;
    String category_name;
    List<WallPaperModel> wallpaper;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public List<WallPaperModel> getWallPaperModelList() {
        return wallpaper;
    }

    public void setWallPaperModelList(List<WallPaperModel> wallpaper) {
        this.wallpaper = wallpaper;
    }
}
