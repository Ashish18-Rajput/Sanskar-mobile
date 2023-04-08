
package com.sanskar.tv.module.HomeModule.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuMaster {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("menu_title")
    @Expose
    private String menuTitle;
    @SerializedName("menu_type_id")
    @Expose
    private String menuTypeId;
    @SerializedName("premium_cat_id")
    @Expose
    private String premium_cat_id;

    @SerializedName("premium_auth_id")
    @Expose
    private String premium_auth_id;

    public String getPremium_cat_id() {
        return premium_cat_id;
    }

    public void setPremium_cat_id(String premium_cat_id) {
        this.premium_cat_id = premium_cat_id;
    }

    public String getPremium_auth_id() {
        return premium_auth_id;
    }

    public void setPremium_auth_id(String premium_auth_id) {
        this.premium_auth_id = premium_auth_id;
    }
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("list")
    @Expose
    private List<MenuMasterList> list = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getMenuTypeId() {
        return menuTypeId;
    }

    public void setMenuTypeId(String menuTypeId) {
        this.menuTypeId = menuTypeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MenuMasterList> getList() {
        return list;
    }

    public void setList(List<MenuMasterList> list) {
        this.list = list;
    }

}
