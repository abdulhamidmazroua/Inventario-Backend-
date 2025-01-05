package com.hameed.inventario.controller;


import com.hameed.inventario.model.dto.request.ProductRequestDTO;
import com.hameed.inventario.model.dto.response.PaginatedResponseDTO;
import com.hameed.inventario.model.dto.response.ProductResponseDTO;
import com.hameed.inventario.model.dto.response.ResponseDTO;
import com.hameed.inventario.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/products")
public class ProductController {

    // properties
    @Value("${pageSize}")
    private int pageSize;

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<ProductResponseDTO>> getAllProducts(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false) Integer size) {
        int finalPageSize = (size == null) ? pageSize : size;
        Page<ProductResponseDTO> productDTOPage = productService.getAllProducts(PageRequest.of(page, finalPageSize));
        return ResponseEntity.ok(new PaginatedResponseDTO<>(200, "Products Retrieved Successfully", productDTOPage)); // 200 OK
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ProductResponseDTO>> getProductById(@PathVariable Long id) {
        ProductResponseDTO productResponseDTO = productService.getProductById(id);
        return ResponseEntity.ok(new ResponseDTO<>(200, "Product Retrieved Successfully", productResponseDTO)); // 200 OK
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public  ResponseEntity<ResponseDTO<ProductResponseDTO>> addProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO resultProductResponseDTO = productService.addProduct(productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(201, "Product Created Successfully", resultProductResponseDTO));  // 201 CREATED
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<ProductResponseDTO>> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO resultProductResponseDTO = productService.updateProduct(id, productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(201, "Product Updated Successfully", resultProductResponseDTO));  // 201 CREATED
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<ProductResponseDTO>> deleteProduct(@PathVariable Long id) {
        productService.removeProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
}
