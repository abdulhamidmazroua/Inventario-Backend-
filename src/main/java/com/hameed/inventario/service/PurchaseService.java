package com.hameed.inventario.service;

import com.hameed.inventario.model.dto.PurchaseCreateDTO;
import com.hameed.inventario.model.dto.PurchaseDTO;
import com.hameed.inventario.model.entity.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PurchaseService {

    // Create a new purchase, returns purchaseNumber
    public String addPurchaseOrder(PurchaseCreateDTO purchaseCreateDTO);

    // Update an existing purchase
    // TODO: this should be authorization restricted
    public void updatePurchase(Long purchaseId, PurchaseCreateDTO purchaseCreateDTO);

    // Remove a purchase
    // TODO: this should be authorization restricted
    public void removePurchase(Long purchaseId);

    // Get all purchases with pagination
    public Page<PurchaseDTO> getAllPurchases(Pageable pageable);

    // Get a purchase by ID
    public PurchaseDTO getPurchaseById(Long purchaseId);

    public PurchaseOrder getPurchaseEntityById(Long purchaseId);

    public void receiveOrder(String purchaseNumber);
}