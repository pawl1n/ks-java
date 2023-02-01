package ua.kishkastrybaie.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.repository.ProductRepository;
import ua.kishkastrybaie.repository.entity.Category;
import ua.kishkastrybaie.repository.entity.Product;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    public Product save(Product product) {
        if (product.getCategory() != null) {
            Category category = categoryService.createIfNotExists(product.getCategory().getName());
            product.setCategory(category);
        }

        return productRepository.save(product);
    }

    public Product update(Product productDetails) {
        Product product = productRepository.findById(productDetails.getId())
                .orElseThrow(EntityNotFoundException::new);

        if (productDetails.getName() != null) {
            product.setName(product.getName());
        }
        if (productDetails.getDescription() != null) {
            product.setDescription(productDetails.getDescription());
        }
        if (productDetails.getCategory() != null) {
            Category category = categoryService.createIfNotExists(product.getCategory().getName());
            product.setCategory(category);
        }
        if (productDetails.getMainImage() != null) {
            product.setMainImage(productDetails.getMainImage());
        }

        return productRepository.save(product);
    }

    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        productRepository.delete(product);
    }
}
