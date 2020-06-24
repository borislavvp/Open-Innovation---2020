package com.kigalimedicine.persistence;

import com.kigalimedicine.persistence.id.PharmacyInsuranceId;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;

@Entity(name = "pharmacy_insurance")
public class PharmacyInsuranceEntity extends PanacheEntityBase {

    @EmbeddedId
    public PharmacyInsuranceId pharmacyInsuranceId;

}
