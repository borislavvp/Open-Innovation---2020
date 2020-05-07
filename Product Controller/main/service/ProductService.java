package com.kigalimedicine.service;

import com.kigalimedicine.persistence.InventoryItemEntity;
import com.kigalimedicine.persistence.PharmacyMemberEntity;
import com.kigalimedicine.persistence.ProductEntity;

import org.h2.util.StringUtils;

import com.kigalimedicine.model.dto.PharmacyDto;
import com.kigalimedicine.model.dto.ProductDto;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductService {
    public List<ProductDto> getAllProducts(Integer page) {
        return ProductEntity.findAll()
                .page(page, 25)
                .stream()
                .map(ProductEntity.class::cast)
                .map(productEntity -> new ProductDto(productEntity.id, productEntity.name, productEntity.ean,
                        productEntity.unit, productEntity.inventoryEntity.size()))
                .collect(Collectors.toList());
    }

    public List<ProductDto> getProduct(String searchResult, Integer page) {
        if (!StringUtils.isNullOrEmpty(searchResult)) {
            return ProductEntity.find("name like ?1 OR ean like ?1", searchResult + '%').stream()
                    .map(ProductEntity.class::cast)
                    .map(productEntity -> new ProductDto(
                            productEntity.id,
                            productEntity.name,
                            productEntity.ean,
                            productEntity.unit, productEntity.inventoryEntity.size()))
                    .collect(Collectors.toList());
        } else {
            return getAllProducts(page);
        }
    }

    @Transactional
    public ProductDto createProduct(ProductDto product, String userId) {
        UserPermission(userId);
        ProductEntity productEntity = new ProductEntity();
        productEntity.ean = product.getEan();
        productEntity.name = product.getName();
        productEntity.unit = product.getUnit();
        productEntity.inventoryEntity = new ArrayList<InventoryItemEntity>();
        productEntity.persist();
        return new ProductDto(productEntity.id, productEntity.name, productEntity.ean, productEntity.unit, 0);
    }

    @Transactional
    public void updateProduct(Long productId, ProductDto product) {
        // TODO : check if the user is admin
        Optional<ProductEntity> productEntity = ProductEntity.findByIdOptional(productId);
        if (!productEntity.isPresent()) {
            throw new RuntimeException();
        }
        productEntity.get().name = product.getName();
        productEntity.get().ean = product.getEan();
        productEntity.get().unit = product.getUnit();

        productEntity.get().persist();

    }

    public void deleteProduct(Long productId) {
        // TODO : check if the user is admin

        Optional<ProductEntity> product = ProductEntity.findByIdOptional(productId);
        if (!product.isPresent()) {
            throw new RuntimeException();
        }

        product.get().delete();
    }

    private void UserPermission(String userId) {
        List<PharmacyMemberEntity> pharmacyMemberships = PharmacyMemberEntity.list("userId", userId);
        if (pharmacyMemberships.size() == 0 || !pharmacyMemberships.stream().map(p -> p.pharmacyEntity.approved).collect(Collectors.toList()).contains(true)) {
            throw new RuntimeException("Not approved user");
        }
    }

    public List<PharmacyDto> getPharmacies(Long productId) {
        Optional<ProductEntity> productEntity = ProductEntity.findByIdOptional(productId);
        if (!productEntity.isPresent()) {
            throw new RuntimeException();
        }
        return productEntity.get().inventoryEntity
                .stream()
                .map(i -> i.pharmacyEntity)
                .map(pharmacyEntity -> new PharmacyDto(
                        pharmacyEntity.id,
                        pharmacyEntity.name,
                        pharmacyEntity.approved,
                        pharmacyEntity.latitude,
                        pharmacyEntity.longitude,
                        pharmacyEntity.address
                ))
                .collect(Collectors.toList());
    }
}
