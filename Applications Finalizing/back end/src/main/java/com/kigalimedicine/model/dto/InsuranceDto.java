package com.kigalimedicine.model.dto;

import javax.validation.constraints.Size;

public class InsuranceDto {
    public InsuranceDto(Long id, String insurance) {
        this.id = id;
        this.insurance = insurance;
    }

    public InsuranceDto() {

    }

    private Long id;

    @Size(min=2)
    private String insurance;

    public Long getId() {
        return id;
    }

    public String getInsurance() {
        return insurance;
    }
}
