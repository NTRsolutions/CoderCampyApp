package com.gmonetix.codercampy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Gaurav Bordoloi on 3/6/2018.
 */

public class Discussion implements Serializable {

    @SerializedName("blog_id")
    public String id;

    public String uid;

    public long timestamp;

    public String message;

    public User user;

    public Discussion(String uid, long timestamp, String message, User user) {
        this.uid = uid;
        this.timestamp = timestamp;
        this.message = message;
        this.user = user;
    }

}
