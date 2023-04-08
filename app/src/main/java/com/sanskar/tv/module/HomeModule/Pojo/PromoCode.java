
package com.sanskar.tv.module.HomeModule.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PromoCode implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("promocode_type")
    @Expose
    private String promocodeType;
    @SerializedName("promocode_value")
    @Expose
    private String promocodeValue;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private String end;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPromocodeType() {
        return promocodeType;
    }

    public void setPromocodeType(String promocodeType) {
        this.promocodeType = promocodeType;
    }

    public String getPromocodeValue() {
        return promocodeValue;
    }

    public void setPromocodeValue(String promocodeValue) {
        this.promocodeValue = promocodeValue;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

}
