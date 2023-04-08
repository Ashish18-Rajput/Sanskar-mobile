
package com.sanskar.tv.module.HomeModule.Pojo.bhajanCategory;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BhajanCatData implements Serializable {

    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("bhajan")
    @Expose
    private List<BhajanCat> bhajan = null;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<BhajanCat> getBhajan() {
        return bhajan;
    }

    public void setBhajan(List<BhajanCat> bhajan) {
        this.bhajan = bhajan;
    }

}
