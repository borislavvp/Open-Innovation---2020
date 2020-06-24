package com.kigalimedicine.model.dto;


import java.sql.Time;
import java.time.DayOfWeek;

public class OpeningTimeDto {

    private String id;

    private DayOfWeek dayOfWeek;

    private String pharmacyId;

    private String startTime;

    private String stopTime;

    public OpeningTimeDto() {
    }

    public OpeningTimeDto(String id, DayOfWeek dayOfWeek, String pharmacyId, String startTime, String stopTime) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.pharmacyId = pharmacyId;
        this.startTime = startTime;
        this.stopTime = stopTime;
    }

    public String getId() {
        return id;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public String getPharmacyId() {
        return pharmacyId;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getStopTime() {
        return stopTime;
    }
}
