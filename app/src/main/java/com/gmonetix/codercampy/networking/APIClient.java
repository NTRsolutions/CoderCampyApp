package com.gmonetix.codercampy.networking;

import com.gmonetix.codercampy.util.CoderCampy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getCacheDir;

/**
 * Created by Gaurav Bordoloi on 2/18/2018.
 */

public class APIClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        int cacheSize = CoderCampy.CACHE_MEMORY * 1024 * 1024; // 100 MB
        Cache cache = new Cache(getCacheDir(), cacheSize);

        /*Gson gson = new GsonBuilder()
                .setLenient()
                .create();*/

        /*HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();*/

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl(CoderCampy.API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }

}
