package com.hameed.inventario.service;

import com.hameed.inventario.model.dto.SaleCreateDTO;
import com.hameed.inventario.model.dto.SaleDTO;
import com.hameed.inventario.model.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SaleService {

    // Create a new sale, returns salesNumber
    public String sell(SaleCreateDTO saleCreateDTO);

    // Update an existing sale.
    // TODO: this should be authorization restricted
    public void updateSale(Long saleId, SaleCreateDTO saleCreateDTO);

    // Remove a sale
    // TODO: this should be authorization restricted
    public void removeSale(Long saleId);

    // Get all sales with pagination
    public Page<SaleDTO> getAllSales(Pageable pageable);

    // Get a sale by ID
    public SaleDTO getSaleById(Long saleId);

    public Sale getSaleEntityById(Long saleId);
}