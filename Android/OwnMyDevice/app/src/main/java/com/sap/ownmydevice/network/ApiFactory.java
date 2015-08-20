package com.sap.ownmydevice.network;

/**
 * Created by i300291 on 06/08/15.
 */
public enum ApiFactory {
    TEAM_API(new TeamApi()),
    MEMBER_API(new MemberApi()),
    DEVICE_API(new DeviceApi());

    private final RetrofitApi instance;

    private ApiFactory(RetrofitApi instance) {
        this.instance = instance;
    }

    public RetrofitApi getApiType() {
        return instance;
    }
}
