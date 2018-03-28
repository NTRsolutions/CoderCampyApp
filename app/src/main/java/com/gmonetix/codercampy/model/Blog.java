package com.gmonetix.codercampy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Gaurav Bordoloi on 3/6/2018.
 */

public class Blog implements Serializable{

    @SerializedName("_id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("image")
    public String image;

    @SerializedName("instructor")
    public String instructor;

    @SerializedName("time")
    public String time;

    @SerializedName("data")
    public String data;

    @SerializedName("languages")
    public List<String> languages;

    @SerializedName("category")
    public String category;

    @Override
    public String toString() {
        return name;
    }

}
