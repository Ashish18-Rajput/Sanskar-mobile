
package com.sanskar.tv.module.HomeModule.Pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Promo {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("category_order")
    @Expose
    private String category_order;
    @SerializedName("season_details")
    @Expose
    private List<SeasonDetail> seasonDetails = null;

    public String getCategory_order() {
        return category_order;
    }

    public void setCategory_order(String category_order) {
        this.category_order = category_order;
    }

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
