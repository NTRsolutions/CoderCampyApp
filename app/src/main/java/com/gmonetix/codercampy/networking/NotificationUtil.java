package com.gmonetix.codercampy.networking;

import com.gmonetix.codercampy.util.CoderCampy;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by Gaurav Bordoloi on 4/2/2018.
 */
public class NotificationUtil {

    public static void subscribeToTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
    }

    public static void unSubscribeToTopic(String topic) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
    }

    public static void subscribeToAllTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(CoderCampy.TOPIC_COURSE);
        FirebaseMessaging.getInstance().subscribeToTopic(CoderCampy.TOPIC_POST);
        FirebaseMessaging.getInstance().subscribeToTopic(CoderCampy.TOPIC_COMPULSORY);
    }

}
