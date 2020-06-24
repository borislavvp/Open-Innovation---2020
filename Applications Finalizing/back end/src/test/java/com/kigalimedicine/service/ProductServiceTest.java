package com.kigalimedicine.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import com.kigalimedicine.enums.Currency;
import com.kigalimedicine.model.dto.InventoryItemDto;
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

        InventoryItemEntity inventoryEntity2 = new InventoryItemEntity();
        inventoryEntity2.currency = "RWF";
        inventoryEntity2.price = new BigDecimal(6000);
        inventoryEntity2.quantity = 5;
        inventoryEntity2.pharmacyEntity = pharmacyEntity;
        inventoryEntity2.product = productEntity;
        inventoryEntity2.persistAndFlush();

        InventoryItemEntity inventoryEntity3 = new InventoryItemEntity();
        inventoryEntity3.currency = "USD";
        inventoryEntity3.price = new BigDecimal(6);
        inventoryEntity3.quantity = 5;
        inventoryEntity3.pharmacyEntity = pharmacyEntity;
        inventoryEntity3.product = productEntity;
        inventoryEntity3.persistAndFlush();

        productEntity.inventoryEntity.add(inventoryEntity3);
        productEntity.inventoryEntity.add(inventoryEntity2);
        productEntity.inventoryEntity.add(inventoryEntity);
        productEntity.persistAndFlush();
        this.productId = productEntity.id;
    }

    @Inject
    ProductService productService;

    @Test
    @Transactional
    public void getProducts_OneProduct() {
        Assertions.assertTrue(productService.getProduct("", 0, null).size() > 0);
    }
    
    @Test
    @Transactional
    public void getProductInventoryItems_OneItem_ShouldReturn_Price() {
        Assertions.assertEquals(5, productService.getProductInventoryItems(this.productId,false).get(0).getPrice().intValue());
    }

    @Test
    @Transactional
    public void getProductInventoryItems_ThreeItems() {
        Assertions.assertEquals(3, productService.getProductInventoryItems(this.productId,false).size());
    }

    @Test
    @Transactional
    public void getProductInventoryItems_OneItem_ShouldReturn_Quantity() {
        Assertions.assertEquals(5, productService.getProductInventoryItems(this.productId,false).get(0).getQuantity());
    }

    @Test
    @Transactional
    public void GetProductByNameOrEan() {
        Assertions.assertTrue(productService.getProduct("Paracet", 0, null).size() > 0);
        Assertions.assertTrue(productService.getProduct("123456789", 0, null).size() > 0);
    }

    @Test
    @Transactional
    public void createProduct_Approved_User() {
        ProductDto productDto = new ProductDto(null, "Nurofen", "123123123", "pcs", 5);

        Assertions.assertEquals("Nurofen", productService.createProduct(productDto, "UserId").getName());
    }

//    @Test
//    @Transactional
//    public void updateProduct_Valid_ProductId() {
//        ProductDto productDto = new ProductDto(this.productId, "Paracetamol 100pcs", "123456789", "pcs", 5);
//        productService.updateProduct(this.productId, productDto);
//
//        Assertions.assertEquals("Paracetamol 100pcs", productService.getProduct("", 0, null).get(0).getName());
//    }

    @Test
    @Transactional
    public void deleteProduct_Valid_ProductId() {
        Long productForDeletingId;
        List<ProductDto> listOfNurofens = productService.getProduct("Nurofen", 0, null);
        if (listOfNurofens.size() > 0) {
            productForDeletingId = listOfNurofens.get(0).getId();
        } else {
            ProductEntity productEntity = new ProductEntity();
            productEntity.ean = "123123123";
            productEntity.name = "Nurofen";
            productEntity.unit = "pcs";
            productEntity.inventoryEntity = new ArrayList<InventoryItemEntity>();
            productEntity.persistAndFlush();

            productForDeletingId = productEntity.id;
        }

        int prevSize = productService.getProduct("", 0, null).size();

        productService.deleteProduct(productForDeletingId);

        Assertions.assertTrue(productService.getProduct("", 0, null).size() < prevSize);
    }

    @Test
    @Transactional
    public void getPharmacies_Valid_ProductId() {
        Assertions.assertEquals(3, productService.getPharmacies(this.productId).size());
    }
    
    
    @Test
    public void Sort_Inventory_Items_Ascending_Order() throws IOException {
        Assertions.assertEquals(Currency.EUR,
                productService.getProductInventoryItems(this.productId, true)
        .get(0).getCurrency());
    }

     @Test
     public void Sort_Inventory_Items_Descending_Order() throws IOException {
        List<InventoryItemDto> result = productService.getProductInventoryItems(this.productId, true);
        Collections.reverse(result);

        Assertions.assertEquals(Currency.RWF, result.get(0).getCurrency());
    }
}
