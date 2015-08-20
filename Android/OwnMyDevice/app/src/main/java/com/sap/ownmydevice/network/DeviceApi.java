package com.sap.ownmydevice.network;

import com.sap.ownmydevice.models.Device;
import com.sap.ownmydevice.models.Member;

import retrofit.Callback;
import retrofit.RestAdapter;

/**
 * Created by i300291 on 06/08/15.
 */
public class DeviceApi extends RetrofitApi {

    private ServiceInterface service;

    public DeviceApi() {
        RestAdapter restAdapter = getRestAdapter();
        service = restAdapter.create(ServiceInterface.class);
    }

    public void getDeviceList( Callback cb){
        service.getDeviceList(cb);
    }

    public void getDevice(String deviceId,Callback cb){
        service.getDevice(deviceId, cb);
    }

    public void addDevice(Device newDevice,Callback<Device> cb){
        service.addDevice(newDevice, cb);
    }

    public void updateDevice(String deviceId, Device device,Callback<Device> cb){
        service.updateDevice(deviceId, device, cb);
    }
    public void deleteDevice(String deviceId,Callback cb){
        service.deleteDevice(deviceId,cb);
    }
}
