package com.example.akshay.tipsatandroidchallenge.service;

import com.example.akshay.tipsatandroidchallenge.parsers.FetchMembersParser;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by akshay on 24/10/15.
 */
public interface WebService {
    @GET("/tipstat")
    void fetchMembers(@QueryMap Map<String, String> map, Callback<FetchMembersParser> callback);
}
