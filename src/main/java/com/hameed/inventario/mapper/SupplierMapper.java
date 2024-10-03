package com.hameed.inventario.mapper;

import com.hameed.inventario.model.dto.SupplierDTO;
import com.hameed.inventario.model.entity.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SupplierMapper {
    // for a singleton mapper
    SupplierMapper  INSTANCE = Mappers.getMapper(SupplierMapper.class);

    SupplierDTO supplierToSupplierDTO(Supplier supplier);

    Supplier supplierDTOToSupplier(SupplierDTO supplierDTO);
}