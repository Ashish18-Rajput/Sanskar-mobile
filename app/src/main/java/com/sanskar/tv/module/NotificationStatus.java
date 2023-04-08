package com.sanskar.tv.module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationStatus {
    @SerializedName("notification_status")
    @Expose
    private String notificationStatus;

    public String getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(String notificationStatus) {
        this.notificationStatus = notificationStatus;
    }


}
