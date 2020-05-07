package com.kigalimedicine.model.dto;

public class ProductDto {
    private Long id;
    private String name;
    private String ean;
    private String unit;
    private Integer amountOfPharmacies;

    public ProductDto(Long id, String name, String ean, String unit, Integer amountOfPharmacies) {
        this.id = id;
        this.name = name;
        this.ean = ean;
        this.unit = unit;
        this.amountOfPharmacies = amountOfPharmacies;
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

    public Integer getAmountOfPharmacies() {
        return amountOfPharmacies;
    }
}
