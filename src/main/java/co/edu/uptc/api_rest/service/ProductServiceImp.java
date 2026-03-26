package co.edu.uptc.api_rest.service;

import co.edu.uptc.api_rest.dto.ProductDTO;
import co.edu.uptc.api_rest.dto.ProductRequestDTO;
import co.edu.uptc.api_rest.dto.ProductResponseDTO;
import co.edu.uptc.api_rest.model.Product;
import co.edu.uptc.api_rest.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.List;

@Service
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private String hostname;

    @Autowired
    public ProductServiceImp(ProductRepository productRepository) {
        this.productRepository = productRepository;
        String resolvedHostname;
        try {
            resolvedHostname = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            resolvedHostname = "unknown";
        }
        this.hostname = resolvedHostname;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(product -> {
            ProductDTO dto = new ProductDTO();
            dto.setName(product.getName());
            dto.setPrice(product.getPrice());
            dto.setProgram(hostname);
            return dto;
        }).toList();
    }

    @Override
    public ProductResponseDTO saveProduct(ProductRequestDTO requestDTO) {
        Product newProduct = new Product(null, requestDTO.getName(), requestDTO.getPrice());
        Product saved = productRepository.save(newProduct);

        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setId(saved.getId());
        responseDTO.setName(saved.getName());
        responseDTO.setPrice(saved.getPrice());
        responseDTO.setMessage("Producto agregado exitosamente.");

        return responseDTO;
    }
}
