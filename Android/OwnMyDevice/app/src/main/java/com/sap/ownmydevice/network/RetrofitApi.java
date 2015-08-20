package com.sap.ownmydevice.network;

import retrofit.RestAdapter;

/**
 * Created by i300291 on 06/08/15.
 */
public class RetrofitApi {
    private static final String BASE_URL = "http://intense-bayou-7716.herokuapp.com" ;
    public static RestAdapter getRestAdapter() {
        RestAdapter.Builder builder = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL);
        return builder.build();
    }
}
