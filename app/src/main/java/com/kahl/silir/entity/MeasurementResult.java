package com.kahl.silir.entity;

import java.util.Calendar;

/**
 * Created by Paskahlis Anjas Prabowo on 02/08/2017.
 */

public class MeasurementResult {
    public static final String EMPTY_URL = "empty";

    private double fvc;
    private double fev1;
    private double pef;
    private MeasurementProfile profile;
    private String vtUrl = EMPTY_URL;
    private String fvUrl = EMPTY_URL;

    public MeasurementResult(double fvc, double fev1, double pef, MeasurementProfile profile) {
        this.fvc = fvc;
        this.fev1 = fev1;
        this.pef = pef;
        this.profile = profile;
    }

    public void setFvc(double fvc) {
        this.fvc = fvc;
    }

    public void setFev1(double fev1) {
        this.fev1 = fev1;
    }

    public void setPef(double pef) {
        this.pef = pef;
    }

    public void setProfile(MeasurementProfile profile) {
        this.profile = profile;
    }

    public void setVtUrl(String vtUrl) {
        this.vtUrl = vtUrl;
    }

    public void setFvUrl(String fvUrl) {
        this.fvUrl = fvUrl;
    }

    public double getFvc() {
        return fvc;
    }

    public double getFev1() {
        return fev1;
    }

    public double getPef() {
        return pef;
    }

    public MeasurementProfile getProfile() {
        return profile;
    }

    public String getVolumeTimeCurveFile() {
        return Calendar.getInstance().getTime().toString() + "-vt.png";
    }

    public String getFlowVolumeLoopFile() {
        return Calendar.getInstance().getTime().toString() + "-fv.png";
    }

    public String getVtUrl() {
        return vtUrl;
    }

    public String getFvUrl() {
        return fvUrl;
    }
}
