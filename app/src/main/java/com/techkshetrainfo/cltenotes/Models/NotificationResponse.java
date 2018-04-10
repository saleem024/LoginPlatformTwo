package com.techkshetrainfo.cltenotes.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by savsoft-3 on 2/1/17.
 */

public class NotificationResponse {

    @SerializedName("notification")
    @Expose
    private List<Notification> notification = null;

    public List<Notification> getNotification() {
        return notification;
    }

    public void setNotification(List<Notification> notification) {
        this.notification = notification;
    }

}