package com.kigalimedicine.service;

import com.kigalimedicine.model.dto.InsuranceDto;
import com.kigalimedicine.model.dto.PharmacyDto;
import com.kigalimedicine.model.dto.PharmacyMemberDto;
import com.kigalimedicine.persistence.*;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PharmacyService {
    public List<PharmacyDto> getOwnedPharmacies(String userId, Integer page, String search) {
        return PharmacyMemberEntity.find("userId = ?1 AND pharmacyEntity.name LIKE ?2", userId, "%" + search + "%").page(page, 25)
                .stream()
                .map(PharmacyMemberEntity.class::cast)
                .map(pharmacyMemberEntity -> pharmacyMemberEntity.pharmacyEntity.toDto())
                .collect(Collectors.toList());
    }

    public List<PharmacyDto> getAllPharmacies(Integer page, String search, Boolean showNotApproved) {
        return PharmacyEntity.find("name LIKE ?1", "%" + search + "%").page(page, 25).stream()
                .map(PharmacyEntity.class::cast)
                .map(PharmacyEntity::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PharmacyDto createPharmacy(PharmacyDto pharmacyDto, String userId) {
        PharmacyEntity pharmacyEntity = new PharmacyEntity();
        pharmacyEntity.name = pharmacyDto.getName();
        pharmacyEntity.address = pharmacyDto.getAddress();
        pharmacyEntity.latitude = pharmacyDto.getLatitude();
        pharmacyEntity.longitude = pharmacyDto.getLongitude();
        // THIS IS CURRENTLY TRUE MEANING THAT WHEN CREATING A PHARMACY IT IS AUTOMATICALLY APPROVED. THIS IS FOR USERTESTING THE DEMO. CHANGE BACK TO pharmacyEntity.approved = false FOR PRODUCTION
        pharmacyEntity.approved = true;
        pharmacyEntity.persist();

        PharmacyMemberEntity pharmacyMemberEntity = new PharmacyMemberEntity();
        pharmacyMemberEntity.pharmacyEntity = pharmacyEntity;
        pharmacyMemberEntity.userId = userId;
        //TODO: userId is (without bearer token) null here. Should work on authorization.
        pharmacyMemberEntity.persist();

        return pharmacyEntity.toDto();
    }

    public PharmacyDto getPharmacyById(Long pharmacyId) {
        PharmacyEntity pharmacy = PharmacyEntity.findById(pharmacyId);
        return pharmacy.toDto();
    }

    @Transactional
    public PharmacyDto editPharmacy(String pharmacyId, PharmacyDto pharmacyDto) {
        PharmacyEntity pharmacyEntity = PharmacyEntity.findById(Long.parseLong(pharmacyId));
        if (pharmacyDto.getLongitude() != null) {
            pharmacyEntity.longitude = pharmacyDto.getLongitude();
        }
        if (pharmacyDto.getLatitude() != null) {
            pharmacyEntity.latitude = pharmacyDto.getLatitude();
        }
        if (pharmacyDto.getApproved() != null) {
            pharmacyEntity.approved = pharmacyDto.getApproved();
        }
        if (pharmacyDto.getApproved() != null) {
            pharmacyEntity.approved = pharmacyDto.getApproved();
        }
        if (pharmacyDto.getAddress() != null) {
            pharmacyEntity.address = pharmacyDto.getAddress();
        }
        if (pharmacyDto.getName() != null) {
            pharmacyEntity.name = pharmacyDto.getName();
        }
        pharmacyEntity.persist();
        return pharmacyEntity.toDto();
    }

    @Transactional
    public void deletePharmacy(String pharmacyId) {
        //Throws NullPointerException if it does not exist:
        PharmacyEntity pharmacy = PharmacyEntity.findById(Long.parseLong(pharmacyId));

        //Todo: fix cascades
        InventoryItemEntity.delete("pharmacyEntity.id", pharmacy.id);
        PharmacyMemberEntity.delete("pharmacyEntity.id", pharmacy.id);
        PharmacyEntity.delete("id", pharmacy.id);
    }

    @Transactional
    public PharmacyMemberDto addPharmacyMember(String pharmacyId, PharmacyMemberDto pharmacyMemberDto) {
        PharmacyMemberEntity pharmacyMemberEntity = new PharmacyMemberEntity();
        pharmacyMemberEntity.pharmacyEntity = PharmacyEntity.findById(Long.parseLong(pharmacyId));
        pharmacyMemberEntity.userId = pharmacyMemberDto.getUserId();
        pharmacyMemberEntity.persist();
        return new PharmacyMemberDto(pharmacyMemberEntity.pharmacyEntity.id.toString(), pharmacyMemberEntity.userId);
    }

    private List<InsuranceDto> insurancesAsDtos(List<InsuranceEntity> insurances) {
        return (insurances == null || insurances.size() == 0) ? new ArrayList<>() : insurances.stream().map(insuranceEntity -> new InsuranceDto(insuranceEntity.id, insuranceEntity.insurance)).collect(Collectors.toList());
    }

    public List<PharmacyMemberDto> getPharmacyMembers(String pharmacyId) {
        PharmacyEntity pharmacy = PharmacyEntity.findById(Long.parseLong(pharmacyId));
        return pharmacy.members.stream()
                .map(pharmacyMemberEntity -> new PharmacyMemberDto(
                        pharmacyMemberEntity.userId,
                        pharmacyMemberEntity.pharmacyEntity.id.toString()))
                .collect(Collectors.toList());
    }
}
