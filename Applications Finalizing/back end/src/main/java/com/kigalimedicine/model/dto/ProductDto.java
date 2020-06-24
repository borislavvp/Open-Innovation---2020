package com.kigalimedicine.model.dto;

import javax.validation.constraints.Size;

public class ProductDto {
    private Long id;
    @Size(min=2)
    private String name;
    @Size(min=2)
    private String ean;
    private String unit;
    private Integer amountOfPharmacies;
    private boolean inPharmacy = false;

    public ProductDto(Long id, String name, String ean, String unit, Integer amountOfPharmacies) {
        this.id = id;
        this.name = name;
        this.ean = ean;
        this.unit = unit;
        this.amountOfPharmacies = amountOfPharmacies;
    }

    public ProductDto() {
    }

    public void setInPharmacy(boolean inPharmacy) {
        this.inPharmacy = inPharmacy;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEan() {
        return ean;
    }

    public String getUnit() {
        return unit;
    }

    public boolean getInPharmacy() {
        return inPharmacy;
    }

    public Integer getAmountOfPharmacies() {
        return amountOfPharmacies;
    }
}
