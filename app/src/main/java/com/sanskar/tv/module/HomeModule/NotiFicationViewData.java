package com.sanskar.tv.module.HomeModule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotiFicationViewData implements Serializable {

        @SerializedName("notification_counter")
        @Expose
        private Integer notificationCounter;

        public Integer getNotificationCounter() {
            return notificationCounter;
        }

        public void setNotificationCounter(Integer notificationCounter) {
            this.notificationCounter = notificationCounter;
        }


}
