package com.kigalimedicine.service;

import com.kigalimedicine.persistence.PharmacyEntity;
import com.kigalimedicine.persistence.InventoryItemEntity;
import com.kigalimedicine.persistence.PharmacyMemberEntity;
import com.kigalimedicine.persistence.ProductEntity;
import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.transaction.Transactional;

@QuarkusTest
public class InventoryServiceTest {

    @Inject
    InventoryService inventoryService;
    @Inject
    ProductService productService;
    
    @Test
    @Transactional
    public void add_item_to_pharmacy() {
        PharmacyEntity pharmacyEntity = new PharmacyEntity();
        pharmacyEntity.approved = true;
        pharmacyEntity.name = "test";
        pharmacyEntity.persist();

        PharmacyMemberEntity pharmacyMemberEntity = new PharmacyMemberEntity();
        pharmacyMemberEntity.pharmacyEntity = pharmacyEntity;
        pharmacyMemberEntity.userId = "add_item_to_pharmacy";
        pharmacyMemberEntity.persist();

        ProductEntity productEntity = new ProductEntity();
        productEntity.ean = "test";
        productEntity.name = "test";
        productEntity.persist();

        inventoryService.additem(pharmacyEntity.id, productEntity.id, "add_item_to_pharmacy");
    }

    public void delete_item_from_pharmacy() {
        PharmacyEntity pharmacyEntity = new PharmacyEntity();
        pharmacyEntity.approved = true;
        pharmacyEntity.name = "test";
        pharmacyEntity.persist();

        PharmacyMemberEntity pharmacyMemberEntity = new PharmacyMemberEntity();
        pharmacyMemberEntity.pharmacyEntity = pharmacyEntity;
        pharmacyMemberEntity.userId = "add_item_to_pharmacy";
        pharmacyMemberEntity.persist();

        PharmacyEntity pharmacyEntity2 = new PharmacyEntity();
        pharmacyEntity2.approved = true;
        pharmacyEntity2.name = "test2";
        pharmacyEntity2.persist();

        PharmacyMemberEntity pharmacyMemberEntity2 = new PharmacyMemberEntity();
        pharmacyMemberEntity2.pharmacyEntity = pharmacyEntity2;
        pharmacyMemberEntity2.userId = "test";
        pharmacyMemberEntity2.persist();

        ProductEntity productEntity = new ProductEntity();
        productEntity.ean = "test";
        productEntity.name = "test";
        productEntity.inventoryEntity = new ArrayList<InventoryItemEntity>();
        productEntity.persistAndFlush();

        inventoryService.additem(pharmacyEntity.id, productEntity.id, "add_item_to_pharmacy");

        inventoryService.additem(pharmacyEntity2.id, productEntity.id, "test");

        inventoryService.deleteItem(pharmacyEntity2.id, productEntity.id, "test");

        Assertions.assertEquals("test", productService.getPharmacies(productEntity.id).get(0).getName());
    }


}
