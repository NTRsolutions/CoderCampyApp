package com.gmonetix.codercampy.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Gaurav Bordoloi on 2/28/2018.
 */

public class Rating implements Serializable{

    @SerializedName("_id")
    public String id;

    @SerializedName("message")
    public String message;

    @SerializedName("rating")
    public int rating;

    public Rating(String id, String message, int rating) {
        this.id = id;
        this.message = message;
        this.rating = rating;
    }
}
