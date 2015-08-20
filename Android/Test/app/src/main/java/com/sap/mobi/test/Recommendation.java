package com.sap.mobi.test;

/**
 * Created by i300291 on 15/08/15.
 */
public class Recommendation {
    private FirstPerson firstPerson;
    private SecondPerson secondPerson;
    private String similarity;

    public Recommendation(FirstPerson firstPerson, SecondPerson secondPerson, String similarity) {
        this.firstPerson = firstPerson;
        this.secondPerson = secondPerson;
        this.similarity = similarity;
    }

    public FirstPerson getFirstPerson() {
        return firstPerson;
    }

    public void setFirstPerson(FirstPerson firstPerson) {
        this.firstPerson = firstPerson;
    }

    public SecondPerson getSecondPerson() {
        return secondPerson;
    }

    public void setSecondPerson(SecondPerson secondPerson) {
        this.secondPerson = secondPerson;
    }

    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }
}
