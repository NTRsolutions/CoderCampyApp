package com.gmonetix.codercampy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by Gaurav Bordoloi on 2/14/2018.
 */

public class Lecture implements Serializable {

    @SerializedName("_id")
    public String id;

    @SerializedName("data")
    public String data;

    @SerializedName("description")
    public String description;

    @SerializedName("name")
    public String name;

    @SerializedName("video")
    public String video;

    @SerializedName("duration")
    public String duration;

    public Lecture(String id, String data, String description, String name, String video, String duration) {
        this.id = id;
        this.data = data;
        this.description = description;
        this.name = name;
        this.video = video;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return name;
    }

}
