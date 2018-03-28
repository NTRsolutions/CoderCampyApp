package com.gmonetix.codercampy.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Gaurav Bordoloi on 2/14/2018.
 */

public class Language implements Serializable {

    @SerializedName("_id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("image")
    public String image;

    @Override
    public String toString() {
        return name;
    }
}
