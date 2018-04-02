package com.gmonetix.codercampy.model;

import java.util.List;

/**
 * Created by Gaurav Bordoloi on 3/30/2018.
 */
public class HomeData {

    public List<String> trending_courses;

    public List<String> popular_courses;

    public List<String> trending_posts;

    public List<String> popular_posts;

    public List<Banner> banners;

    @Override
    public String toString() {
        return "HomeData{" +
                "trending_courses=" + trending_courses +
                ", popular_courses=" + popular_courses +
                ", trending_posts=" + trending_posts +
                ", popular_posts=" + popular_posts +
                ", banners=" + banners +
                '}';
    }
}
