package com.gmonetix.codercampy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Gaurav Bordoloi on 3/6/2018.
 */

public class CourseDiscussion implements Serializable{

    @SerializedName("course_id")
    public String id;

    @SerializedName("discussions")
    List<Discussion> discussions;

}
