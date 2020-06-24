package com.kigalimedicine.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "inventory_item")
public class InventoryItemEntity extends PanacheEntity {

    public int quantity;

    public BigDecimal price;

    public String currency;

    @ManyToOne
    @JoinColumn(name = "product", nullable = false)
    public ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "pharmacy", nullable = false)
    public PharmacyEntity pharmacyEntity;
}
