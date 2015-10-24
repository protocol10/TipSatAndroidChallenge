package com.example.akshay.tipsatandroidchallenge;

import com.example.akshay.tipsatandroidchallenge.service.WebService;
import com.example.akshay.tipsatandroidchallenge.utils.UniversalAppConstants;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.orm.SugarApp;
import com.squareup.okhttp.OkHttpClient;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by akshay on 24/10/15.
 */
public class BaseApplication extends SugarApp {
    WebService webService;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeRetrofit();
    }

    public WebService initializeRetrofit() {
        webService = initializeRestAdapter().create(WebService.class);
        return webService;
    }

    public WebService getWebService() {
        return webService;
    }

    public RestAdapter initializeRestAdapter() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        RestAdapter.Builder builder = new RestAdapter.Builder();
        builder.setEndpoint(UniversalAppConstants.BASE_URL);
        if (BuildConfig.DEBUG) {
            builder.setLogLevel(RestAdapter.LogLevel.FULL);
        } else {
            builder.setLogLevel(RestAdapter.LogLevel.NONE);
        }
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
        builder.setClient(new OkClient(okHttpClient));
        builder.setConverter(new GsonConverter(gson));
        return builder.build();
    }
}
