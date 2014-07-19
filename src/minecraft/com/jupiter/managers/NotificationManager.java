package com.jupiter.managers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by corey on 16/07/14.
 */
public class NotificationManager {

    private List<Notification> notifications = new ArrayList<>();

    public NotificationManager() {
    }

    public void addNotification(String message) {
        notifications.add(new Notification(message));
        if (notifications.size() > 16)
            notifications.remove(0);
    }

    public class Notification {
        public final String message;
        public final long addTime;

        protected Notification(String message) {
            this.message = message;
            addTime = System.currentTimeMillis();
        }

    }

}
