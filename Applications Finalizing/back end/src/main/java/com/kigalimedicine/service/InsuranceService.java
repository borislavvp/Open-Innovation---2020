package com.kigalimedicine.service;

import com.kigalimedicine.model.dto.InsuranceDto;
import com.kigalimedicine.persistence.*;
import com.kigalimedicine.persistence.id.PharmacyInsuranceId;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class InsuranceService {

    public List<InsuranceDto> getInsurances(String search, Integer page) {
        return InsuranceEntity.find("insurance LIKE ?1", "%" + search + "%")
                .page(page, 20)
                .stream()
                .map(InsuranceEntity.class::cast)
                .map(this::insuranceEntityToInsuranceDto)
                .collect(Collectors.toList());
    }

    public InsuranceDto addInsurance(InsuranceDto insuranceDto) {
        InsuranceEntity insuranceEntity = new InsuranceEntity();
        insuranceEntity.insurance = insuranceDto.getInsurance();

        insuranceEntity.persist();

        return insuranceEntityToInsuranceDto(insuranceEntity);
    }

    public void deleteInsurance(Long insuranceId) {
        // TODO: fix cascades
        PharmacyInsuranceEntity.delete("pharmacyInsuranceId.insurance.id", insuranceId);
        InsuranceEntity.findById(insuranceId).delete();
    }

    public void updateInsurance(Long insuranceId, InsuranceDto insuranceDto) {
        Optional<InsuranceEntity> insuranceEntity = InsuranceEntity.findByIdOptional(insuranceId);
        if (!insuranceEntity.isPresent()) {
            throw new NotFoundException();
        }
        insuranceEntity.get().insurance = insuranceDto.getInsurance();

        insuranceEntity.get().persist();
    }

    public void addInsuranceToPharmacy(Long insuranceId, Long pharmacyId, String userId) {
        if (!pharmacyPermission(userId, pharmacyId)) {
            throw new RuntimeException("Not authorized to access pharmacy");
        }

        if (PharmacyInsuranceEntity.find("pharmacyInsuranceId.pharmacy.id = ?1 AND pharmacyInsuranceId.insurance.id = ?2", pharmacyId, insuranceId).firstResultOptional().isPresent()) {
            throw new RuntimeException("Pharmacy already has insurancy");
        }

        Optional<InsuranceEntity> insurance = InsuranceEntity.findByIdOptional(insuranceId);

        if (!insurance.isPresent()) {
            throw new RuntimeException("This insurance does not exist");
        }
        PharmacyInsuranceEntity pharmacyInsuranceEntity = new PharmacyInsuranceEntity();
        pharmacyInsuranceEntity.pharmacyInsuranceId = new PharmacyInsuranceId(insurance.get(), PharmacyEntity.findById(pharmacyId));
        pharmacyInsuranceEntity.persist();
    }

    public void removeInsuranceFromPharmacy(Long insuranceId, Long pharmacyId, String userId) {
        if (!pharmacyPermission(userId, pharmacyId)) {
            throw new RuntimeException("Not authorized to access pharmacy");
        }

        PharmacyInsuranceEntity.delete("pharmacyInsuranceId.pharmacy.id = ?1 AND pharmacyInsuranceId.insurance.id = ?2", pharmacyId, insuranceId);
    }

    private boolean pharmacyPermission(String userId, Long pharmacyId) {
        Optional<PharmacyEntity> pharmacyEntity = PharmacyEntity.findByIdOptional(pharmacyId);
        if (!pharmacyEntity.isPresent() && pharmacyEntity.get().approved) {
            return false;
        }
        return PharmacyMemberEntity.find("pharmacyEntity.id = :pharmacyId AND userId = :userId", Parameters.with("pharmacyId", pharmacyId).and("userId", userId)).firstResultOptional().isPresent();
    }

    private InsuranceDto insuranceEntityToInsuranceDto(InsuranceEntity insuranceEntity) {
        return new InsuranceDto(insuranceEntity.id, insuranceEntity.insurance);
    }
}
