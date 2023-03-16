package ua.kishkastrybaie.product;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kishkastrybaie.category.Category;
import ua.kishkastrybaie.category.CategoryDto;
import ua.kishkastrybaie.category.CategoryModelAssembler;
import ua.kishkastrybaie.category.CategoryNotFoundException;
import ua.kishkastrybaie.image.Image;
import ua.kishkastrybaie.image.ImageDto;
import ua.kishkastrybaie.image.ImageModelAssembler;
import ua.kishkastrybaie.image.ImageNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;
  private final CategoryModelAssembler categoryModelAssembler;
  private final ProductMapper productMapper;
  private final RepresentationModelAssembler<Product, ProductDto> productModelAssembler;
  private final PagedResourcesAssembler<Product> pagedResourcesAssembler;
  private final ImageModelAssembler imageModelAssembler;

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
  @Transactional
  public CategoryDto getProductCategory(Long id) {
    Product product =
        productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

    Category category =
        Optional.ofNullable(product.getCategory())
            .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

    return categoryModelAssembler.toModel(category);
  }

  @Override
  @Transactional
  public ImageDto getProductImage(Long id) {
    Product product =
        productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

    Image image =
        Optional.ofNullable(product.getMainImage())
            .orElseThrow(() -> new ImageNotFoundException("Image not found"));

    return imageModelAssembler.toModel(image);
  }
}
