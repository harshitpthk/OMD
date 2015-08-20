package com.sap.ownmydevice.network;

import com.sap.ownmydevice.models.Team;


import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;

/**
 * Created by i300291 on 06/08/15.
 */
public class TeamApi extends RetrofitApi {
    private ServiceInterface service;

    public TeamApi() {
        RestAdapter restAdapter = getRestAdapter();
        service = restAdapter.create(ServiceInterface.class);
    }

    public void getTeamList(String listMembers,Callback<ArrayList<Team>> cb){
        service.getTeamList(listMembers,cb);
    }

    public void getTeam(String teamId,Callback<Team> cb){
        service.getTeam(teamId, cb);
    }

    public void addTeam(Team newTeam,Callback<Team> cb){
        service.addTeam(newTeam, cb);
    }

    public void updateTeam(String teamId,Team team,Callback<Team> cb){
        service.updateTeam(teamId, team, cb);
    }
    public void deleteTeam(String teamId,Callback<String> cb){
        service.deleteTeam(teamId,cb);
    }
}
