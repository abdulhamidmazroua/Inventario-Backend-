package com.hameed.inventario.service.impl;

import com.hameed.inventario.exception.ResourceNotFoundException;
import com.hameed.inventario.mapper.UnitOfMeasureMapper;
import com.hameed.inventario.model.dto.UnitOfMeasureDTO;
import com.hameed.inventario.model.entity.UnitOfMeasure;
import com.hameed.inventario.repository.UOMRepository;
import com.hameed.inventario.service.UnitOfMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UOMRepository uomRepository;

    @Autowired
    public UnitOfMeasureServiceImpl(UOMRepository uomRepository) {
        this.uomRepository = uomRepository;
    }


    @Override
    public void createUnitOfMeasure(UnitOfMeasureDTO unitOfMeasureDTO) {
        UnitOfMeasure unitOfMeasure = UnitOfMeasureMapper.INSTANCE.unitOfMeasureDTOToUnitOfMeasure(unitOfMeasureDTO);
        uomRepository.save(unitOfMeasure);
    }

    @Override
    public void updateUnitOfMeasure(Long unitOfMeasureId, UnitOfMeasureDTO unitOfMeasureDTO) {
        uomRepository.findById(unitOfMeasureId).ifPresentOrElse(
                unitOfMeasure -> {
                    // map fields of dto to unitOfMeasure
                    unitOfMeasure.setUomName(unitOfMeasureDTO.getUomName());
                    unitOfMeasure.setUomCode(unitOfMeasureDTO.getUomCode());
                    unitOfMeasure.setDescription(unitOfMeasureDTO.getDescription());
                    // save
                    uomRepository.save(unitOfMeasure);
                },
                () -> {
                    throw new ResourceNotFoundException("UnitOfMeasure with this Id: " + unitOfMeasureId + " could not be found");
                }
        );
    }

    @Override
    public void deleteUnitOfMeasure(Long unitOfMeasureId) {
        uomRepository.findById(unitOfMeasureId).ifPresentOrElse(
                unitOfMeasure -> {
                    // handling the link with other entities before deleting
                    unitOfMeasure.getProducts().forEach(product -> product.setPrimaryUom(null));

                    uomRepository.delete(unitOfMeasure);
                },
                () -> {
                    throw new ResourceNotFoundException("UnitOfMeasure with this Id: " + unitOfMeasureId + " could not be found");
                }
        );
    }

    @Override
    public UnitOfMeasureDTO getUnitOfMeasureById(Long unitOfMeasureId) {
        UnitOfMeasure unitOfMeasure = getUnitOfMeasureEntityById(unitOfMeasureId);
        return UnitOfMeasureMapper.INSTANCE.unitOfMeasureToUnitOfMeasureDTO(unitOfMeasure);
    }

    @Override
    public Page<UnitOfMeasureDTO> getAllUnitOfMeasures(Pageable pageable) {
        Page<UnitOfMeasure> pageUnitOfMeasures = uomRepository.findAll(pageable);
        return pageUnitOfMeasures.map(UnitOfMeasureMapper.INSTANCE::unitOfMeasureToUnitOfMeasureDTO);
    }

    @Override
    public UnitOfMeasure getUnitOfMeasureEntityById(Long unitOfMeasureId) {
        return uomRepository.findById(unitOfMeasureId).orElseThrow(() -> new ResourceNotFoundException("UnitOfMeasure with this Id: " + unitOfMeasureId + " could not be found"));
    }
}
