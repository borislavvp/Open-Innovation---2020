package com.kigalimedicine.model.dto;

public class PharmacyMemberDto {
    private String userId;
    private String pharmacyId;

    public PharmacyMemberDto(String userId, String pharmacyId) {
        this.userId = userId;
        this.pharmacyId = pharmacyId;
    }

    public PharmacyMemberDto() {
    }

    public String getUserId() {
        return userId;
    }

    public String getPharmacyId() {
        return pharmacyId;
    }
}
