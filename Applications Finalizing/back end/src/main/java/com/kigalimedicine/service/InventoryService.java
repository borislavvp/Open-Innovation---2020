package com.kigalimedicine.service;

import com.kigalimedicine.model.dto.InventoryItemDto;
import com.kigalimedicine.model.dto.ProductDto;
import com.kigalimedicine.persistence.InventoryItemEntity;
import com.kigalimedicine.persistence.PharmacyEntity;
import com.kigalimedicine.persistence.PharmacyMemberEntity;
import com.kigalimedicine.persistence.ProductEntity;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class InventoryService {

    public List<InventoryItemDto> getPharmacyInventory(Long pharmacyId, int page, String userId, String search, Boolean quantityOrderAsc){
        if(!pharmacyPermission(userId, pharmacyId)){
            throw new RuntimeException("Not authorized to access pharmacy");
        }

        return InventoryItemEntity.find("pharmacyEntity.id = ?1 AND (product.name LIKE ?2 OR product.ean LIKE ?2)", getSort(quantityOrderAsc), pharmacyId, "%" + search + "%")
                .page(page, 20)
                .stream()
                .map(InventoryItemEntity.class::cast)
                .map(this::inventoryEntityToInventoryDto)
                .collect(Collectors.toList());
    }

    public InventoryItemDto additem(Long pharmacyId, Long productId, String userId){
        if(!pharmacyPermission(userId, pharmacyId)){
            throw new RuntimeException("Not authorized to access pharmacy");
        }
        //checks if pharmacy already has item with productId
        if(InventoryItemEntity.find("pharmacyEntity.id = :pharmacyId AND product.id = :productId", Parameters.with("pharmacyId", pharmacyId).and("productId", productId)).firstResultOptional().isPresent()){
            throw new RuntimeException();
        }

        InventoryItemEntity inventoryItemEntity = new InventoryItemEntity();

        //Defaults
        inventoryItemEntity.currency = "USD";
        inventoryItemEntity.price = new BigDecimal(0);
        inventoryItemEntity.quantity = 0;

        inventoryItemEntity.pharmacyEntity = PharmacyEntity.findById(pharmacyId);
        inventoryItemEntity.product = ProductEntity.findById(productId);
        inventoryItemEntity.product.inventoryEntity.add(inventoryItemEntity);
        inventoryItemEntity.persist();

        return inventoryEntityToInventoryDto((inventoryItemEntity));
    }


    public void updateItem(Long inventoryItemId, InventoryItemDto inventoryItemDto, String userId){
        Optional<InventoryItemEntity> inventoryItem =  InventoryItemEntity.findByIdOptional(inventoryItemId);
        if(!inventoryItem.isPresent()){
            throw new RuntimeException();
        }

        if(!pharmacyPermission(userId, inventoryItem.get().pharmacyEntity.id)){
            throw new RuntimeException("Not authorized to access pharmacy");
        }
        inventoryItem.get().quantity = inventoryItemDto.getQuantity();
        inventoryItem.get().price = inventoryItemDto.getPrice();
        inventoryItem.get().currency = inventoryItemDto.getCurrency().name();
    }

    public void deleteItem(Long pharmacyId, Long productId, String userId){
        if(!pharmacyPermission(userId, pharmacyId)){
            throw new RuntimeException("Not authorized to access pharmacy");
        }

        InventoryItemEntity inventoryItem = InventoryItemEntity.find(
                "pharmacyEntity.id = :pharmacyId AND product.id = :productId", 
                 Parameters.with("pharmacyId", pharmacyId).and("productId", productId)).firstResult();
        if(inventoryItem == null){
            throw new RuntimeException();
        }
        inventoryItem.product.inventoryEntity.remove(inventoryItem);
        inventoryItem.delete();
    }

    private InventoryItemDto inventoryEntityToInventoryDto(InventoryItemEntity entity){
        return new InventoryItemDto(entity.id, productEntityToProductDTO(entity.product),entity.pharmacyEntity.toDto(), entity.quantity, entity.price, entity.currency);
    }

    private ProductDto productEntityToProductDTO(ProductEntity entity) {
        return new ProductDto(entity.id, entity.name, entity.ean, entity.unit, entity.inventoryEntity.size());
    }

    private Sort getSort(Boolean quantityOrderAsc){
        return quantityOrderAsc ? Sort.ascending("quantity") : Sort.descending("quantity");
    }

    private boolean pharmacyPermission(String userId, Long pharmacyId) {
        Optional<PharmacyEntity> pharmacyEntity = PharmacyEntity.findByIdOptional(pharmacyId);
        if (!pharmacyEntity.isPresent() && pharmacyEntity.get().approved) {
            return false;
        }
        return PharmacyMemberEntity
                .find("pharmacyEntity.id = :pharmacyId AND userId = :userId",
                        Parameters.with("pharmacyId", pharmacyId).and("userId", userId))
                .firstResultOptional().isPresent();
    }
}
