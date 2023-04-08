
package com.sanskar.tv.EPG;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TvGuiderMaster {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Days")
    @Expose
    private List<List<Day>> days = null;
    @SerializedName("error")
    @Expose
    private List<Object> error = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<List<Day>> getDays() {
        return days;
    }

    public void setDays(List<List<Day>> days) {
        this.days = days;
    }

    public List<Object> getError() {
        return error;
    }

    public void setError(List<Object> error) {
        this.error = error;
    }

}
