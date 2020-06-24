package com.kigalimedicine.persistence;

import com.kigalimedicine.model.dto.InsuranceDto;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity(name = "insurance")
public class InsuranceEntity extends PanacheEntity {
    public String insurance;

    @ManyToMany(mappedBy = "insurances")
    public List<PharmacyEntity> pharmacies;

    public InsuranceDto toDto() {
        return new InsuranceDto(id, insurance);
    }
}
