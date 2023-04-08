package com.sanskar.tv.module.HomeModule.Pojo;

public class VideoResponseNew
{
    private Category[] category;

    private Videos[] videos;

    private Banners[] banners;

    public Category[] getCategory ()
    {
        return category;
    }

    public void setCategory (Category[] category)
    {
        this.category = category;
    }

    public Videos[] getVideos ()
    {
        return videos;
    }

    public void setVideos (Videos[] videos)
    {
        this.videos = videos;
    }

    public Banners[] getBanners ()
    {
        return banners;
    }

    public void setBanners (Banners[] banners)
    {
        this.banners = banners;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [category = "+category+", videos = "+videos+", banners = "+banners+"]";
    }
}