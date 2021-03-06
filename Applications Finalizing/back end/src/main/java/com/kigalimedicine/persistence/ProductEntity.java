package com.kigalimedicine.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity(name = "product")
public class ProductEntity extends PanacheEntity {
    public String name;

    public String ean;

    public String unit;

    @OneToMany(mappedBy = "product",cascade = {CascadeType.REMOVE})
    public List<InventoryItemEntity> inventoryEntity = new ArrayList<>();
}
