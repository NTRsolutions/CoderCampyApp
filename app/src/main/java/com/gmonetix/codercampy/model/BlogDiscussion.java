package com.gmonetix.codercampy.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Gaurav Bordoloi on 3/6/2018.
 */

public class BlogDiscussion implements Serializable{

    @SerializedName("blog_id")
    public String id;

    public String uid;

    public long timestamp;

    public String message;

    public User user;

}
