package com.kigalimedicine.persistence.id;

import com.kigalimedicine.persistence.InsuranceEntity;
import com.kigalimedicine.persistence.PharmacyEntity;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class PharmacyInsuranceId implements Serializable {
    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "insuranceId", nullable = false)
    public InsuranceEntity insurance;

    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "pharmacyId", nullable = false)
    public PharmacyEntity pharmacy;

    public PharmacyInsuranceId(InsuranceEntity insurance, PharmacyEntity pharmacy) {
        this.insurance = insurance;
        this.pharmacy = pharmacy;
    }

    public PharmacyInsuranceId() {
    }
}
