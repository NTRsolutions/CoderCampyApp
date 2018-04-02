package com.gmonetix.codercampy.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.gmonetix.codercampy.database.LectureTypeConverter;
import com.gmonetix.codercampy.database.TypeConverter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Gaurav Bordoloi on 2/14/2018.
 */

@Entity
public class Course implements Serializable{

    @PrimaryKey
    @NonNull
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

    @TypeConverters(TypeConverter.class)
    @SerializedName("languages")
    public List<String> languages;

    @TypeConverters(LectureTypeConverter.class)
    @SerializedName("lecture")
    public List<Lecture> lectures;

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
                '}';
    }

}
