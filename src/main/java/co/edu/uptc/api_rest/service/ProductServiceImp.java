package co.edu.uptc.api_rest.service;

import co.edu.uptc.api_rest.dto.ProductDTO;
import co.edu.uptc.api_rest.dto.ProductRequestDTO;
import co.edu.uptc.api_rest.dto.ProductResponseDTO;
import co.edu.uptc.api_rest.model.Product;
import co.edu.uptc.api_rest.storage.ProductFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImp implements ProductService {

    private ProductFileStorage storage;

    @Value("${program.id}")
    private String program;

    @Autowired
    public ProductServiceImp(ProductFileStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products =  storage.readProducts();
        return products.stream().map(product -> {
            ProductDTO dto = new ProductDTO();
            dto.setName(product.getName());
            dto.setPrice(product.getPrice());
            dto.setProgram(program);
            return dto;
        }).toList();
    }

    @Override
    public ProductResponseDTO saveProduct(ProductRequestDTO requestDTO) {
        List<Product> products = storage.readProducts();
        Long currentId = 1L;
        for (Product p : products) {
            if (p.getId() >= currentId) {
                currentId = p.getId() + 1;
            }
        }
        Product newProduct = new Product(currentId, requestDTO.getName(), requestDTO.getPrice());
        products.add(newProduct);

        storage.saveProducts(products);

        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setId(newProduct.getId());
        responseDTO.setName(newProduct.getName());
        responseDTO.setPrice(newProduct.getPrice());
        responseDTO.setMessage("Producto agregado exitosamente");

        return responseDTO;
    }
}
