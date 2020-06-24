package com.kigalimedicine.persistence;

import com.kigalimedicine.model.dto.OpeningTimeDto;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.sql.Time;
import java.time.DayOfWeek;

@Entity
public class OpeningTimeEntity extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "pharmacy", nullable = false)
    public PharmacyEntity pharmacyEntity;

    @Enumerated(EnumType.STRING)
    public DayOfWeek day;

    public Time startTime;

    public Time stopTime;

    public OpeningTimeDto toDto() {
        return new OpeningTimeDto(this.id.toString(),
                this.day,
                this.pharmacyEntity.id.toString(),
                this.startTime.toString(),
                this.stopTime.toString());
    }
}
