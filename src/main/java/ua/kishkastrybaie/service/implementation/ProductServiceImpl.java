package ua.kishkastrybaie.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.repository.ProductRepository;
import ua.kishkastrybaie.repository.entity.Category;
import ua.kishkastrybaie.repository.entity.Product;
import ua.kishkastrybaie.service.CategoryService;
import ua.kishkastrybaie.service.ProductService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    public Product create(Product product) {
        if (product.getCategory() != null) {
            Category category = categoryService.createIfNotExists(product.getCategory().getName());
            product.setCategory(category);
        }

        return productRepository.save(product);
    }

    public Product update(Long id, Product productDetails) {
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

    public Product replace(Long id, Product newProduct) {
        return productRepository.findById(id).map(product -> {
            product.setName(newProduct.getName());
            product.setDescription(newProduct.getDescription());
            product.setMainImage(newProduct.getMainImage());
            product.setCategory(categoryService.createIfNotExists(newProduct.getCategory().getName()));
            return productRepository.save(product);
        }).orElseGet(() -> {
            newProduct.setId(id);
            return productRepository.save(newProduct);
        });
    }

    public void deleteById(Long id) {
        Product product = findById(id);
        productRepository.delete(product);
    }
}
