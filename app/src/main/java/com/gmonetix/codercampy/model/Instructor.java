package com.gmonetix.codercampy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Gaurav Bordoloi on 2/21/2018.
 */

public class Instructor  implements Serializable{

    @SerializedName("_id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("email")
    public String email;

    @SerializedName("image")
    public String image;

    @SerializedName("phone")
    public String phone;

    @SerializedName("bio")
    public String bio;

    @SerializedName("languages")
    public List<String> languages;

    @SerializedName("categories")
    public List<String> categories;

    @Override
    public String toString() {
        return name;
    }

    public String getLanguagesAsString() {
        StringBuilder res = new StringBuilder();
        for (String s : languages) {
            if (!res.toString().isEmpty())
                res.append(",");
            res.append(s);
        }
        return res.toString().trim();
    }

    public String getCategoriesAsString() {
        StringBuilder res = new StringBuilder();
        for (String s : categories) {
            if (!res.toString().isEmpty())
                res.append(",");
            res.append(s);
        }
        return res.toString().trim();
    }

}
