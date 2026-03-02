package co.edu.uptc.api_rest.service;

import co.edu.uptc.api_rest.dto.ProductDTO;
import co.edu.uptc.api_rest.dto.ProductRequestDTO;
import co.edu.uptc.api_rest.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    ProductResponseDTO saveProduct(ProductRequestDTO requestDTO);
}
