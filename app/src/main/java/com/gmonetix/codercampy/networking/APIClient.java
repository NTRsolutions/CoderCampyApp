package com.gmonetix.codercampy.networking;

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

        int cacheSize = 100 * 1024 * 1024; // 100 MB
        Cache cache = new Cache(getCacheDir(), cacheSize);

        /*HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();*/

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.2:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();



        return retrofit;
    }

}
