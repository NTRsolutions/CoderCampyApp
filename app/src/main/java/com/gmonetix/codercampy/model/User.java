package com.gmonetix.codercampy.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by Gaurav Bordoloi on 3/27/2018.
 */

public class User {

    @SerializedName("_id")
    public String id;

    public String email;

    public String name;

    public String image;

    public String phone;

    public String provider;

    public String uid;

    public List<String> favourites;

    public User(String email, String name, String image, String provider, String uid) {
        this.email = email;
        this.name = name;
        this.image = image;
        this.provider = provider;
        this.uid = uid;
    }

}
