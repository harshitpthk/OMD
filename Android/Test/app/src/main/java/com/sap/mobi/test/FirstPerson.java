package com.sap.mobi.test;

/**
 * Created by i300291 on 15/08/15.
 */
public class FirstPerson {
    private String name;
    private String url;

    public FirstPerson(String name, String url) {
        this.name =name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
