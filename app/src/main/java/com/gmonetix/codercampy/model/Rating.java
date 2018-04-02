package com.gmonetix.codercampy.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Gaurav Bordoloi on 2/28/2018.
 */

public class Rating implements Serializable{

    public String uid;

    public int rating;

    public String message;

    public Rating(String uid, int rating, String message) {
        this.uid = uid;
        this.rating = rating;
        this.message = message;
    }
}
