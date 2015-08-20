package com.sap.ownmydevice.network;

import com.sap.ownmydevice.models.Device;
import com.sap.ownmydevice.models.Member;
import com.sap.ownmydevice.models.Team;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by i300291 on 06/08/15.
 */
public interface ServiceInterface {
    @GET("/teams/members/{listMembers}")
    void getTeamList(@Path("listMembers") String listMembers, Callback<ArrayList<Team>> cb);

    @GET("/getteam/{teamId}")
    void getTeam(@Path("teamId") String teamId,Callback<Team> cb);

    @GET("/members/device/{listDevices}")
    void getMemberList(@Path("listDevices") String listDevices,Callback<ArrayList<Member>> cb);

    @GET("/getmember/{memberId}")
    void getMember(@Path("memberId") String memberId,Callback<Member> cb);


    @GET("/devices")
    void getDeviceList(Callback<ArrayList<Device>> deviceList);

    @GET("/getdevice/{deviceId}")
    void getDevice(@Path("deviceId") String deviceId,Callback<Device> cb);

    @POST("/team/add")
    void addTeam(@Body Team newTeam, Callback<Team> cb);

    @POST("/member/add")
    void addMember(@Body Member newMember,Callback<Member> cb);

    @POST("/device/add")
    void addDevice(@Body Device newDevice,Callback<Device> cb);

    @PUT("/team/update/{teamId}")
    void updateTeam(@Path("teamId") String teamId,@Body Team team,Callback<Team> cb);

    @PUT("/member/update/{memberId}")
    void updateMember(@Path("memberId") String memberId,@Body Member member,Callback<Member> cb);

    @PUT("/device/update/{deviceId}")
    void updateDevice(@Path("deviceId") String deviceId,@Body Device device,Callback<Device> cb);

    @DELETE("/team/{teamId}")
    void deleteTeam(@Path("teamId") String teamId, Callback<String> cb);

    @DELETE("/member/{memberId}")
    void deleteMember(@Path("memberId") String memberId,Callback <String> cb);

    @DELETE("/device/{deviceId}")
    void deleteDevice(@Path("deviceId") String deviceId,Callback <String> cb);

}
