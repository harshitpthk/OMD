package com.sap.ownmydevice.network;

import com.sap.ownmydevice.models.Member;
import com.sap.ownmydevice.models.Team;

import retrofit.Callback;
import retrofit.RestAdapter;

/**
 * Created by i300291 on 06/08/15.
 */
public class MemberApi extends RetrofitApi {
    private ServiceInterface service;

    public MemberApi() {
        RestAdapter restAdapter = getRestAdapter();
        service = restAdapter.create(ServiceInterface.class);
    }

    public void getMemberList(String listDevices, Callback cb){
        service.getMemberList(listDevices, cb);
    }

    public void getMember(String memberId,Callback cb){
        service.getMember(memberId, cb);
    }

    public void addMember(Member newMember,Callback cb){
        service.addMember(newMember, cb);
    }

    public void updateMember(String memberId,Member member,Callback cb){
        service.updateMember(memberId, member, cb);
    }
    public void deleteMember(String memberId,Callback cb){
        service.deleteMember(memberId,cb);
    }
}
