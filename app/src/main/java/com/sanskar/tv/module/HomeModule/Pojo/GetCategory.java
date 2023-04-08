
package com.sanskar.tv.module.HomeModule.Pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCategory {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("season_details")
    @Expose
    private List<SeasonDetail> seasonDetails = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public List<SeasonDetail> getSeasonDetails() {
        return seasonDetails;
    }

    public void setSeasonDetails(List<SeasonDetail> seasonDetails) {
        this.seasonDetails = seasonDetails;
    }

}
