package com.sanskar.tv.module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sanskar.tv.module.HomeModule.NotiFicationViewData;

import java.io.Serializable;
import java.util.List;

public class NotificationViewResonse implements Serializable {


        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("data")
        @Expose
        private NotiFicationViewData notiFicationViewData;
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

    public NotiFicationViewData getNotiFicationViewData() {
        return notiFicationViewData;
    }

    public void setNotiFicationViewData(NotiFicationViewData notiFicationViewData) {
        this.notiFicationViewData = notiFicationViewData;
    }

    public List<Object> getError() {
        return error;
    }

    public void setError(List<Object> error) {
        this.error = error;
    }
}
