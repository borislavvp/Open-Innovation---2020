package com.kigalimedicine.persistence;

import com.kigalimedicine.model.dto.PharmacyMemberDto;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "pharmacy_member")
public class PharmacyMemberEntity extends PanacheEntity {

    public String userId;

    @ManyToOne
    @JoinColumn(name = "pharmacy", nullable = false)
    public PharmacyEntity pharmacyEntity;

    public PharmacyMemberDto toDto() {
        return new PharmacyMemberDto(userId, pharmacyEntity.id.toString());
    }
}
