package com.gmonetix.codercampy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by Gaurav Bordoloi on 2/14/2018.
 */

public class Category implements Serializable {

    @SerializedName("_id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("image")
    public String image;

    public Category(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    @Override
    public String toString() {
        return name;
    }

}
