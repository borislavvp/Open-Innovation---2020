package com.kigalimedicine.service;

import com.kigalimedicine.model.dto.OpeningTimeDto;
import com.kigalimedicine.persistence.OpeningTimeEntity;
import com.kigalimedicine.persistence.PharmacyEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.sql.Time;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class OpeningTimeService {

    @Transactional
    public OpeningTimeDto addOpeningTime(OpeningTimeDto openingTimeDto, String pharmacyId) {
        OpeningTimeEntity openingTimeEntity = new OpeningTimeEntity();
        openingTimeEntity.pharmacyEntity = PharmacyEntity.findById(Long.parseLong(pharmacyId));
        Objects.requireNonNull(openingTimeEntity.pharmacyEntity);

        openingTimeEntity.day = openingTimeDto.getDayOfWeek();
        openingTimeEntity.startTime = Time.valueOf(openingTimeDto.getStartTime());
        openingTimeEntity.stopTime = Time.valueOf(openingTimeDto.getStopTime());
        openingTimeEntity.persist();

        return openingTimeEntity.toDto();
    }

    public List<OpeningTimeDto> getOpeningTimes(String pharmacyId) {
        return OpeningTimeEntity.find("pharmacyEntity.id", Long.parseLong(pharmacyId))
                .stream()
                .map(OpeningTimeEntity.class::cast)
                .map(OpeningTimeEntity::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteOpeningTime(String openingTimeId) {
        OpeningTimeEntity.findById(Long.parseLong(openingTimeId)).delete();
    }
}
