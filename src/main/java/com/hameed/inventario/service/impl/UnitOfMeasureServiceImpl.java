package com.hameed.inventario.service.impl;

import com.hameed.inventario.exception.DuplicateCodeException;
import com.hameed.inventario.exception.ResourceNotFoundException;
import com.hameed.inventario.mapper.UnitOfMeasureMapper;
import com.hameed.inventario.model.dto.basic.UnitOfMeasureDTO;
import com.hameed.inventario.model.entity.UnitOfMeasure;
import com.hameed.inventario.repository.UOMRepository;
import com.hameed.inventario.service.UnitOfMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UOMRepository uomRepository;
    private final UnitOfMeasureMapper unitOfMeasureMapper;

    @Autowired
    public UnitOfMeasureServiceImpl(UOMRepository uomRepository, UnitOfMeasureMapper unitOfMeasureMapper) {
        this.uomRepository = uomRepository;
        this.unitOfMeasureMapper = unitOfMeasureMapper;
    }


    @Override
    public UnitOfMeasureDTO createUnitOfMeasure(UnitOfMeasureDTO unitOfMeasureDTO) {
        // map to unit of measure object
        UnitOfMeasure unitOfMeasure = unitOfMeasureMapper.unitOfMeasureDTOToUnitOfMeasure(unitOfMeasureDTO);
        // service-validation
        if (uomRepository.findByUomCode(unitOfMeasureDTO.getUomCode()).isPresent()) {
            throw new DuplicateCodeException("Unit Of Measure code " + unitOfMeasure.getUomCode() + " already exists");
        }
        // save
        UnitOfMeasure resultUnitOfMeasure =  uomRepository.save(unitOfMeasure);
        // return the result as DTO
        return unitOfMeasureMapper.unitOfMeasureToUnitOfMeasureDTO(resultUnitOfMeasure);
    }

    @Override
    @Transactional
    public UnitOfMeasureDTO updateUnitOfMeasure(Long unitOfMeasureId, UnitOfMeasureDTO unitOfMeasureDTO) {

        Optional<UnitOfMeasure> optionalUnitOfMeasure = uomRepository.findById(unitOfMeasureId);
        if(optionalUnitOfMeasure.isPresent()) {
            UnitOfMeasure unitOfMeasure = optionalUnitOfMeasure.get();
            // map fields of dto to unitOfMeasure
            unitOfMeasure.setUomName(unitOfMeasureDTO.getUomName());
            unitOfMeasure.setUomCode(unitOfMeasureDTO.getUomCode());
            unitOfMeasure.setDescription(unitOfMeasureDTO.getDescription());

            // save
            UnitOfMeasure resultUnitOfMeasure =  uomRepository.save(unitOfMeasure);

            // return the updated DTO
            return unitOfMeasureMapper.unitOfMeasureToUnitOfMeasureDTO(resultUnitOfMeasure);
        } else {
            throw new ResourceNotFoundException("UnitOfMeasure with this Id: " + unitOfMeasureId + " could not be found");
        }
    }

    @Override
    @Transactional
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
        return unitOfMeasureMapper.unitOfMeasureToUnitOfMeasureDTO(unitOfMeasure);
    }

    @Override
    public Page<UnitOfMeasureDTO> getAllUnitOfMeasures(Pageable pageable) {
        Page<UnitOfMeasure> pageUnitOfMeasures = uomRepository.findAll(pageable);
        return pageUnitOfMeasures.map(unitOfMeasureMapper::unitOfMeasureToUnitOfMeasureDTO);
    }

    @Override
    public UnitOfMeasure getUnitOfMeasureEntityById(Long unitOfMeasureId) {
        return uomRepository.findById(unitOfMeasureId).orElseThrow(() -> new ResourceNotFoundException("UnitOfMeasure with this Id: " + unitOfMeasureId + " could not be found"));
    }
}
