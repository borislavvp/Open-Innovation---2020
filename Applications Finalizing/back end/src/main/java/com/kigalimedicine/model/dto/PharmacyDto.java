package com.kigalimedicine.model.dto;

import javax.validation.constraints.Size;
import java.util.List;

public class PharmacyDto {
    private Long id;
    @Size(min = 2)
    private String name;
    private Boolean approved;
    private Double latitude;
    private Double longitude;
    @Size(min = 2)
    private String address;
    private List<InsuranceDto> insurances;
    private List<OpeningTimeDto> openingTimes;

    public PharmacyDto(Long id,
                       String name,
                       Boolean approved,
                       Double latitude,
                       Double longitude,
                       String address,
                       List<InsuranceDto> insurances,
                       List<OpeningTimeDto> openingTimes) {
        this.id = id;
        this.name = name;
        this.approved = approved;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.insurances = insurances;
        this.openingTimes = openingTimes;
    }

    public PharmacyDto() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getApproved() {
        return approved;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public List<InsuranceDto> getInsurances() {return insurances;}

    public List<OpeningTimeDto> getOpeningTimes() {
        return openingTimes;
    }
}
