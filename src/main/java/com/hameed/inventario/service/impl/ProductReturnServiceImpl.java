package com.hameed.inventario.service.impl;

import com.hameed.inventario.exception.ResourceNotFoundException;
import com.hameed.inventario.mapper.ProductReturnMapper;
import com.hameed.inventario.model.dto.ProductReturnCreateDTO;
import com.hameed.inventario.model.dto.ProductReturnDTO;
import com.hameed.inventario.model.entity.Product;
import com.hameed.inventario.model.entity.ProductReturn;
import com.hameed.inventario.model.entity.Customer;
import com.hameed.inventario.repository.ProductReturnRepository;
import com.hameed.inventario.service.ProductService;
import com.hameed.inventario.service.ProductReturnService;
import com.hameed.inventario.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductReturnServiceImpl implements ProductReturnService {

    private final ProductReturnRepository productReturnRepository;
    private final ProductService productService;
    private final CustomerService customerService;

    @Autowired
    public ProductReturnServiceImpl(ProductReturnRepository productReturnRepository, ProductService productService, CustomerService customerService) {
        this.productReturnRepository = productReturnRepository;
        this.productService = productService;
        this.customerService = customerService;
    }

    @Override
    public void addProductReturn(ProductReturnCreateDTO productReturnCreateDTO) {
        ProductReturn productReturn = ProductReturnMapper.INSTANCE.productReturnCreateDTOToProductReturn(productReturnCreateDTO);
        // calling services to get product and uom
        Product productReturnProduct = productService.getProductEntityById(productReturnCreateDTO.getProductId());
        Customer customer = customerService.getCustomerEntityById(productReturnCreateDTO.getCustomerId());
        productReturn.setProduct(productReturnProduct);
        productReturn.setCustomer(customer);
        productReturnRepository.save(productReturn);
    }

    @Override
    public void updateProductReturn(Long productReturnId, ProductReturnCreateDTO productReturnCreateDTO) {
        productReturnRepository.findById(productReturnId).ifPresentOrElse(
                productReturn -> {
                    // map fields of dto to productReturn
                    
                    // calling services to get product and uom
                    Product productReturnProduct = productService.getProductEntityById(productReturnCreateDTO.getProductId());
                    Customer customer = customerService.getCustomerEntityById(productReturnCreateDTO.getCustomerId());
                    productReturn.setProduct(productReturnProduct);
                    productReturn.setCustomer(customer);

                    // save
                    productReturnRepository.save(productReturn);
                },
                () -> {
                    throw new ResourceNotFoundException("ProductReturn with this Id: " + productReturnId + " could not be found");
                }
        );
    }

    @Override
    public void removeProductReturn(Long productReturnId) {
        productReturnRepository.deleteById(productReturnId);
    }

    @Override
    public ProductReturnDTO getProductReturnById(Long productReturnId) {
        ProductReturn productReturn = getProductReturnEntityById(productReturnId);
        return ProductReturnMapper.INSTANCE.productReturnToProductReturnDTO(productReturn);
    }

    @Override
    public Page<ProductReturnDTO> getAllProductReturns(Pageable pageable) {
        Page<ProductReturn> pageProductReturns = productReturnRepository.findAll(pageable);
        return pageProductReturns.map(ProductReturnMapper.INSTANCE::productReturnToProductReturnDTO);
    }

    @Override
    public ProductReturn getProductReturnEntityById(Long productReturnId) {
        return productReturnRepository.findById(productReturnId).orElseThrow(() -> new ResourceNotFoundException("ProductReturn with this Id: " + productReturnId + " could not be found"));
    }
}
