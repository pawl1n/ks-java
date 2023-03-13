package ua.kishkastrybaie.product;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.category.Category;
import ua.kishkastrybaie.category.CategoryDto;
import ua.kishkastrybaie.category.CategoryModelAssembler;
import ua.kishkastrybaie.category.CategoryNotFoundException;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;
  private final CategoryModelAssembler categoryModelAssembler;
  private final ProductModelAssembler productModelAssembler;
  private final ProductMapper productMapper;
  private final PagedResourcesAssembler<Product> pagedResourcesAssembler;

  @Override
  public CollectionModel<ProductDto> findAll(Pageable pageable) {
    return pagedResourcesAssembler.toModel(
        productRepository.findAll(pageable), productModelAssembler);
  }

  @Override
  public ProductDto findById(Long id) {
    return productModelAssembler.toModel(
        productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id)));
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
