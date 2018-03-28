package com.gmonetix.codercampy.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Gaurav Bordoloi on 3/6/2018.
 */

public class Discussion implements Serializable {

    @SerializedName("uid")
    public String uid;

    @SerializedName("timestamp")
    public String timestamp;

    @SerializedName("message")
    public String message;

    @SerializedName("reply")
    public List<Discussion> replies;

}
