package com.kigalimedicine.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import com.kigalimedicine.model.dto.ProductDto;
import com.kigalimedicine.persistence.InventoryItemEntity;
import com.kigalimedicine.persistence.PharmacyEntity;
import com.kigalimedicine.persistence.PharmacyMemberEntity;
import com.kigalimedicine.persistence.ProductEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductServiceTest {
    private Long productId;
    
    @BeforeAll
    @Transactional
    public void createPharmacyAndPharmacyMember() {
        PharmacyEntity pharmacyEntity = new PharmacyEntity();
        pharmacyEntity.address = "testaddr";
        pharmacyEntity.approved = true;
        pharmacyEntity.latitude = 51.5555;
        pharmacyEntity.longitude = 4.4444;
        pharmacyEntity.name = "Test Pharmacy";
        pharmacyEntity.persist();

        PharmacyMemberEntity pharmacyMemberEntity = new PharmacyMemberEntity();
        pharmacyMemberEntity.pharmacyEntity = pharmacyEntity;
        pharmacyMemberEntity.userId = "UserId";
        pharmacyMemberEntity.persist();

        ProductEntity productEntity = new ProductEntity();
        productEntity.ean = "123456789";
        productEntity.name = "Paracetamol";
        productEntity.unit = "pcs";
        productEntity.inventoryEntity = new ArrayList<InventoryItemEntity>(); 
        productEntity.persistAndFlush();

        InventoryItemEntity inventoryEntity = new InventoryItemEntity();
        inventoryEntity.currency = "EUR";
        inventoryEntity.price = new BigDecimal(5);
        inventoryEntity.quantity = 5;
        inventoryEntity.pharmacyEntity = pharmacyEntity;
        inventoryEntity.product = productEntity;
        inventoryEntity.persistAndFlush();

        productEntity.inventoryEntity.add(inventoryEntity);
        productEntity.persistAndFlush();
        this.productId = productEntity.id;
    }

    @Inject 
    ProductService productService;

    @Test
    @Transactional
    public void getProducts_OneProduct() {
        Assertions.assertEquals(1, productService.getProduct("", 0).size());
    }

    @Test
    @Transactional
    public void GetProductByNameOrEan() {
        Assertions.assertEquals(1, productService.getProduct("Paracet", 0).size());
        Assertions.assertEquals(1, productService.getProduct("123456789", 0).size());
    }

    @Test
    @Transactional
    public void createProduct_Approved_User() {
        ProductDto productDto = new ProductDto(null, "Nurofen", "123123123", "pcs", 5);

        Assertions.assertEquals("Nurofen", productService.createProduct(productDto, "UserId").getName());
    }
    
    @Test
    @Transactional
    public void updateProduct_Valid_ProductId() {
        ProductDto productDto = new ProductDto(this.productId, "Paracetamol 100pcs", "123456789", "pcs", 5);
        productService.updateProduct(this.productId, productDto);

        Assertions.assertEquals("Paracetamol 100pcs", productService.getProduct("", 0).get(0).getName());
    }
    
    @Test
    @Transactional
    public void deleteProduct_Valid_ProductId() {
        Long productForDeletingId;
        List<ProductDto> listOfNurofens = productService.getProduct("Nurofen", 0);
        if (listOfNurofens.size() > 0) {
            productForDeletingId = listOfNurofens.get(0).getId();
        }
        else {
              ProductEntity productEntity = new ProductEntity();
              productEntity.ean = "123123123";
              productEntity.name = "Nurofen";
              productEntity.unit = "pcs";
              productEntity.inventoryEntity = new ArrayList<InventoryItemEntity>(); 
              productEntity.persistAndFlush();

              productForDeletingId = productEntity.id;
        }

        Assertions.assertEquals(2, productService.getProduct("", 0).size());

        productService.deleteProduct(productForDeletingId);

        Assertions.assertEquals(1, productService.getProduct("", 0).size());
    }
    
    @Test
    @Transactional
    public void getPharmacies_Valid_ProductId() {
        Assertions.assertEquals(
            1, 
            productService.getPharmacies(this.productId).size()
         );
     }
}
