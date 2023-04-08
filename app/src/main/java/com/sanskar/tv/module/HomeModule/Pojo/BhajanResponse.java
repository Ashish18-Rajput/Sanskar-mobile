package com.sanskar.tv.module.HomeModule.Pojo;

import java.io.Serializable;

public class BhajanResponse implements Serializable {
    public Bhajan[] bhajan;

    public String category_name;

    public Bhajan[] getBhajan() {
        return bhajan;
    }

    public void setBhajan(Bhajan[] bhajan) {
        this.bhajan = bhajan;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    @Override
    public String toString() {
        return "ClassPojo [bhajan = " + bhajan + ", category_name = " + category_name + "]";
    }
}
