package com.gmonetix.codercampy.util;

import com.bumptech.glide.request.RequestOptions;

/**
 * Created by Gaurav Bordoloi on 2/26/2018.
 */

public class GlideOptions {

    public static RequestOptions getRequestOptions(int error, int placeHolder) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(error);
        requestOptions.placeholder(placeHolder);
        return requestOptions;
    }

    public static String getThumbnailLink(String videoId) {
        return "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
    }

}
