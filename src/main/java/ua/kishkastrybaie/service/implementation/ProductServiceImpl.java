package ua.kishkastrybaie.service.implementation;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.controller.assembler.CategoryModelAssembler;
import ua.kishkastrybaie.controller.assembler.ProductModelAssembler;
import ua.kishkastrybaie.controller.dto.CategoryDto;
import ua.kishkastrybaie.controller.dto.ProductDto;
import ua.kishkastrybaie.controller.dto.ProductRequestDto;
import ua.kishkastrybaie.controller.dto.mapper.ProductMapper;
import ua.kishkastrybaie.exception.CategoryNotFoundException;
import ua.kishkastrybaie.exception.ProductNotFoundException;
import ua.kishkastrybaie.repository.ProductRepository;
import ua.kishkastrybaie.repository.entity.Category;
import ua.kishkastrybaie.repository.entity.Product;
import ua.kishkastrybaie.service.ProductService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;
  private final CategoryModelAssembler categoryModelAssembler;
  private final ProductModelAssembler productModelAssembler;
  private final ProductMapper productMapper;

  @Override
  public CollectionModel<ProductDto> findAll() {
    return productModelAssembler.toCollectionModel(productRepository.findAll());
  }

  @Override
  public ProductDto findById(Long id) {
    return productModelAssembler.toModel(
        productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id)));
  }

  @Override
  public boolean existsById(Long id) {
    return productRepository.existsById(id);
  }

  @Override
  public ProductDto create(ProductRequestDto productRequestDto) {
    Product product = productMapper.toDomain(productRequestDto);

    return productModelAssembler.toModel(productRepository.save(product));
  }

  @Override
  public ProductDto replace(Long id, ProductRequestDto productRequestDto) {
    Product productRequest = productMapper.toDomain(productRequestDto);

    Product product =
        productRepository
            .findById(id)
            .map(
                p -> {
                  p.setName(productRequest.getName());
                  p.setDescription(productRequest.getDescription());
                  p.setCategory(productRequest.getCategory());
                  p.setMainImage(productRequest.getMainImage());
                  return productRepository.save(p);
                })
            .orElseThrow(() -> new ProductNotFoundException(id));

    return productModelAssembler.toModel(product);
  }

  @Override
  public void deleteById(Long id) {
    Product product =
        productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    productRepository.delete(product);
  }

  @Override
  public CategoryDto getProductCategory(Long id) {
    Product product =
        productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    Category category =
        Optional.ofNullable(product.getCategory())
            .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

    return categoryModelAssembler.toModel(category);
  }
}
