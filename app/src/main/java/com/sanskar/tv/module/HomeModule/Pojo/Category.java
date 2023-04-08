package com.sanskar.tv.module.HomeModule.Pojo;

public class Category
{
    boolean isClicked= false;

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    private String id="";

    private String category_name="All";

    private String status;

    private String creation_time;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getCategory_name ()
    {
        return category_name;
    }

    public void setCategory_name (String category_name)
    {
        this.category_name = category_name;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
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
        return "ClassPojo [id = "+id+", category_name = "+category_name+", status = "+status+", creation_time = "+creation_time+"]";
    }
}