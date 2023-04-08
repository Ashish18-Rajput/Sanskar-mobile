
package com.sanskar.tv.module.HomeModule.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Version {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("android")
    @Expose
    private String android;
    @SerializedName("is_hard_update_android")
    @Expose
    private String isHardUpdateAndroid;
    @SerializedName("ios")
    @Expose
    private String ios;
    @SerializedName("is_hard_update_ios")
    @Expose
    private String isHardUpdateIos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAndroid() {
        return android;
    }

    public void setAndroid(String android) {
        this.android = android;
    }

    public String getIsHardUpdateAndroid() {
        return isHardUpdateAndroid;
    }

    public void setIsHardUpdateAndroid(String isHardUpdateAndroid) {
        this.isHardUpdateAndroid = isHardUpdateAndroid;
    }

    public String getIos() {
        return ios;
    }

    public void setIos(String ios) {
        this.ios = ios;
    }

    public String getIsHardUpdateIos() {
        return isHardUpdateIos;
    }

    public void setIsHardUpdateIos(String isHardUpdateIos) {
        this.isHardUpdateIos = isHardUpdateIos;
    }

}
