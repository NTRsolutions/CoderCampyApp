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

    @SerializedName("discussions")
    public List<Discussion> discussions;

}
