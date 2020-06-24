package com.kigalimedicine.service;

import java.util.Comparator;

import com.kigalimedicine.enums.Currency;
import com.kigalimedicine.model.dto.InventoryItemDto;

class SortedByPrice implements Comparator<InventoryItemDto> {
    public double USDtoEURexchangeRate = 0;
    public double USDtoRWFexchangeRate = 0;
    
    public int compare(InventoryItemDto i1, InventoryItemDto i2) {            
        Double price1 = i1.getPrice().doubleValue();
        Double price2 = i2.getPrice().doubleValue();

        if (i1.getCurrency() == Currency.EUR) {
            price1 /= USDtoEURexchangeRate;
        } else if (i1.getCurrency() == Currency.RWF) {
            price1 /= USDtoEURexchangeRate;
        }
        
        if (i2.getCurrency() == Currency.EUR) {
            price2 /= USDtoRWFexchangeRate;
        } else if (i2.getCurrency() == Currency.RWF) {
            price2 /= USDtoRWFexchangeRate;
        }
	      //ascending order
        return price1.compareTo(price2);
    }
}