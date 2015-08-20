package com.sap.ownmydevice.models;

import java.util.ArrayList;

/**
 * Created by i300291 on 06/08/15.
 */
public class Member {
    private String url;
    private String id;
    private String memberName;
    private String memberEID;
    private String memberEmail;
    private String memberTeam;

    public Member(String url, String memberName, String memberEID, String memberEmail, String memberTeam) {
        this.url = url;
        this.memberName = memberName;
        this.memberEID = memberEID;
        this.memberEmail = memberEmail;
        this.memberTeam = memberTeam;
    }
    public Member(String url,String id, String memberName, String memberEID, String memberEmail, String memberTeam) {
        this.url = url;
        this.id = id;
        this.memberName = memberName;
        this.memberEID = memberEID;
        this.memberEmail = memberEmail;
        this.memberTeam = memberTeam;
    }

    private ArrayList<Device> deviceList;

    public ArrayList<Device> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(ArrayList<Device> deviceList) {
        this.deviceList = deviceList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberEID() {
        return memberEID;
    }

    public void setMemberEID(String memberEID) {
        this.memberEID = memberEID;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getMemberTeam() {
        return memberTeam;
    }

    public void setMemberTeam(String memberTeam) {
        this.memberTeam = memberTeam;
    }
}
