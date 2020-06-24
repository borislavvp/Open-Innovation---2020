package com.kigalimedicine.persistence;

import com.kigalimedicine.model.dto.PharmacyDto;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "pharmacy")
public class PharmacyEntity extends PanacheEntity {

    public String name;
    public Double latitude;
    public Double longitude;
    public Boolean approved;
    public String address;

    @ManyToMany(cascade = {CascadeType.DETACH})
    @JoinTable(
            name = "pharmacy_insurance",
            joinColumns = {@JoinColumn(name = "pharmacyId")},
            inverseJoinColumns = {@JoinColumn(name = "insuranceId")}
    )
    public List<InsuranceEntity> insurances;

    @OneToMany(mappedBy = "pharmacyEntity", cascade = {CascadeType.REMOVE})
    public List<PharmacyMemberEntity> members;

    @OneToMany(mappedBy = "pharmacyEntity", cascade = {CascadeType.REMOVE})
    public List<InventoryItemEntity> items;

    @OneToMany(mappedBy = "pharmacyEntity", cascade = {CascadeType.REMOVE})
    public List<OpeningTimeEntity> openingTimes;

    public PharmacyDto toDto() {
        return new PharmacyDto(id,
                name,
                approved,
                latitude,
                longitude,
                address,
                (insurances == null || insurances.size() == 0) ? new ArrayList<>() :
                        insurances.stream().map(InsuranceEntity::toDto).collect(Collectors.toList()),
                (openingTimes == null || openingTimes.size() == 0) ? new ArrayList<>() :
                        openingTimes.stream().map(OpeningTimeEntity::toDto).collect(Collectors.toList()));
    }
}
