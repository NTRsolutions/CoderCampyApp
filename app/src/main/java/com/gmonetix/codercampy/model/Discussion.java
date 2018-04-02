package com.gmonetix.codercampy.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Gaurav Bordoloi on 3/6/2018.
 */

public class Discussion implements Serializable {

    public String uid;

    public String timestamp;

    public String message;

    public Discussion(String uid, String timestamp, String message) {
        this.uid = uid;
        this.timestamp = timestamp;
        this.message = message;
    }

}
