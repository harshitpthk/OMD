package com.sap.ownmydevice.network;

/**
 * Created by i300291 on 06/08/15.
 */
public class NetworkController {

    private static NetworkController instance;

    public RetrofitApi getApiOfType(ApiFactory type) {
        return type.getApiType();
    }

    public static synchronized NetworkController getInstance() {
        if(instance == null) {
            instance = new NetworkController();
        }
        return instance;
    }

}
