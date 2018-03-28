package com.gmonetix.codercampy.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Gaurav Bordoloi on 2/14/2018.
 */

public class Course implements Serializable{

    @SerializedName("_id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("category")
    public String category;

    @SerializedName("overview")
    public String overview;

    @SerializedName("description")
    public String description;

    @SerializedName("image")
    public String image;

    @SerializedName("instructor")
    public String instructor;

    @SerializedName("is_active")
    public boolean is_active;

    @SerializedName("languages")
    public List<String> languages;

    @SerializedName("lecture")
    public List<Lecture> lectures;

    @SerializedName("rating")
    public List<Rating> ratings;

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", overview='" + overview + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", instructor='" + instructor + '\'' +
                ", is_active=" + is_active +
                ", languages=" + languages +
                ", lectures=" + lectures +
                ", ratings=" + ratings +
                '}';
    }
}
