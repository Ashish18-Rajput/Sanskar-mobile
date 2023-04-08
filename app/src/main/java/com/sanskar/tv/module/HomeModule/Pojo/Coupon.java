
package com.sanskar.tv.module.HomeModule.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coupon {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("coupon_tilte")
    @Expose
    private String couponTilte;
    @SerializedName("coupon_type")
    @Expose
    private String couponType;
    @SerializedName("coupon_value")
    @Expose
    private String couponValue;
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

    public String getCouponTilte() {
        return couponTilte;
    }

    public void setCouponTilte(String couponTilte) {
        this.couponTilte = couponTilte;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(String couponValue) {
        this.couponValue = couponValue;
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
