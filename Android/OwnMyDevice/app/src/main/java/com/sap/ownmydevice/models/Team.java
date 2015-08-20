package com.sap.ownmydevice.models;

import java.util.ArrayList;

/**
 * Created by i300291 on 06/08/15.
 */
public class Team {
    private String url;
    private String id;

    private String teamName;
    private ArrayList<Member> members;




    public Team(String url, String id, String teamName) {
        this.url = url;
        this.id = id;
        this.teamName = teamName;
    }

    public Team(String teamName) {
        this.teamName = teamName;
    }
    public Team(String url,String teamName) {
        this.teamName = teamName;
        this.url = url;
    }
    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }
}
