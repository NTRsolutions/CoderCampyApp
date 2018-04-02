package com.gmonetix.codercampy.model;

import java.io.Serializable;

/**
 * Created by Gaurav Bordoloi on 4/1/2018.
 */
public class Library implements Serializable {

    public String name, link;

    public Library(String name, String link) {
        this.name = name;
        this.link = link;
    }

}
