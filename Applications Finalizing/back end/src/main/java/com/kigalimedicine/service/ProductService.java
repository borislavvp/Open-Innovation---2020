package com.kigalimedicine.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kigalimedicine.model.dto.InsuranceDto;
import com.kigalimedicine.model.dto.InventoryItemDto;
import com.kigalimedicine.persistence.*;

import org.h2.util.StringUtils;

import com.kigalimedicine.model.dto.PharmacyDto;
import com.kigalimedicine.model.dto.ProductDto;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
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

    public List<ProductDto> getProduct(String searchResult, Integer page, Long pharmacyId) {
        List<ProductDto> products;
        if (!StringUtils.isNullOrEmpty(searchResult)) {
            products = ProductEntity.find("name like ?1 OR ean like ?1", '%' + searchResult + '%').stream()
                    .map(ProductEntity.class::cast)
                    .map(productEntity -> new ProductDto(
                            productEntity.id,
                            productEntity.name,
                            productEntity.ean,
                            productEntity.unit, productEntity.inventoryEntity.size()))
                    .collect(Collectors.toList());
        } else {
            products = getAllProducts(page);
        }
        if (pharmacyId != null) {
            updateInPharmacy(products, pharmacyId);
        }
        return products;
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

    private void updateInPharmacy(List<ProductDto> products, Long pharmacyId) {
        List<InventoryItemEntity> pharmacyItems = InventoryItemEntity.list("pharmacyEntity.id = ?1 AND product.id IN ?2", pharmacyId, products.stream().map(ProductDto::getId).collect(Collectors.toList()));

        products.forEach((p) -> {
            if (pharmacyItems.stream().anyMatch((p2) -> p2.product.id.equals(p.getId()))) {
                p.setInPharmacy(true);
            }
        });
    }

    public List<PharmacyDto> getPharmacies(Long productId) {
        Optional<ProductEntity> productEntity = ProductEntity.findByIdOptional(productId);
        if (!productEntity.isPresent()) {
            throw new RuntimeException();
        }
        return productEntity.get().inventoryEntity
                .stream()
                .map(i -> i.pharmacyEntity)
                .map(PharmacyEntity::toDto)
                .collect(Collectors.toList());
    }

    public List<InventoryItemDto> getProductInventoryItems(Long productId,boolean sortByPrice) {
        Optional<ProductEntity> productEntity = ProductEntity.findByIdOptional(productId);
        if (!productEntity.isPresent()) {
            throw new RuntimeException();
        }
        List<InventoryItemDto> items = productEntity.get().inventoryEntity.stream()
                    .map(i -> new InventoryItemDto(i.id,
                            new ProductDto(i.product.id, i.product.name, i.product.ean, i.product.unit,
                                    i.product.inventoryEntity.size()),
                            i.pharmacyEntity.toDto(), i.quantity, i.price, i.currency))
                    .collect(Collectors.toList());
        if (!sortByPrice) {
            return items;
        } else {
            List<InventoryItemDto> result = items;
        
            String usd_eur_rwf = "https://free.currconv.com/api/v7/convert?q=USD_RWF,USD_EUR&compact=ultra&apiKey=181b6d5665036dbc92b4";

            try {
                // Making Request
                URL url = new URL(usd_eur_rwf);
                HttpURLConnection request = (HttpURLConnection) url.openConnection();
                request.connect();
                
                int responsecode = request.getResponseCode();

                if (responsecode != 200) {
                    throw new RuntimeException();
                }
                
                Scanner sc = new Scanner(url.openStream());
                String parsedString = "";
                while (sc.hasNext()) {
                    parsedString += sc.nextLine();
                }
                sc.close();

                // Convert to JSON
                JSONObject response = JSON.parseObject(parsedString);
                
                SortedByPrice sortByPriceASC = new SortedByPrice();
                sortByPriceASC.USDtoEURexchangeRate = response.getDouble("USD_EUR");
                sortByPriceASC.USDtoRWFexchangeRate = response.getDouble("USD_RWF");;
            
                Collections.sort(result,sortByPriceASC) ;
                return result;

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }
}
