package com.kigalimedicine.model.dto;

import com.kigalimedicine.enums.Currency;
import com.kigalimedicine.model.dto.ProductDto;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class InventoryItemDto {
    public InventoryItemDto(Long id, ProductDto product,PharmacyDto pharmacy, int quantity, BigDecimal price, String currency){
        this.id = id;
        this.product = product;
        this.pharmacy = pharmacy;
        this.quantity = quantity;
        this.price = price;
        this.currency = Currency.valueOf(currency);
    }

    public InventoryItemDto() {

    }

    private Long id;

    private ProductDto product;

    private PharmacyDto pharmacy;

    @Min(value = 0)
    private int quantity;

    @Min(value = 0)
    private BigDecimal price;

    private Currency currency;

    public Long getId() {return id;}

    public ProductDto getProduct() {return product;}
    
    public PharmacyDto getPharmacy() {return pharmacy;}

    public int getQuantity(){return quantity;}

    public BigDecimal getPrice(){return price;}

    public Currency getCurrency(){return currency;}
}
