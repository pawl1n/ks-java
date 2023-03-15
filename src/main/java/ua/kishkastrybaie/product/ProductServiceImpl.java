package ua.kishkastrybaie.product;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
import ua.kishkastrybaie.product.item.*;
import ua.kishkastrybaie.variation.Variation;
import ua.kishkastrybaie.variation.VariationRepository;
import ua.kishkastrybaie.variation.option.VariationOption;
import ua.kishkastrybaie.variation.option.VariationOptionId;
import ua.kishkastrybaie.variation.option.VariationOptionRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;
  private final CategoryModelAssembler categoryModelAssembler;
  private final ProductMapper productMapper;
  private final RepresentationModelAssembler<Product, ProductDto> productModelAssembler;
  private final PagedResourcesAssembler<Product> pagedResourcesAssembler;
  private final ProductItemRepository productItemRepository;
  private final RepresentationModelAssembler<ProductItem, ProductItemDto> productItemModelAssembler;
  private final VariationOptionRepository variationOptionRepository;
  private final VariationRepository variationRepository;
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

  @Override
  public CollectionModel<ProductItemDto> getProductVariations(Long id) {
    return productItemModelAssembler.toCollectionModel(
        productItemRepository.findAllByProductId(id));
  }

  @Override
  @Transactional
  public ProductItemDto addVariation(Long id, ProductItemRequestDto productItemRequestDto) {
    //    Variation variation =
    // variationRepository.getReferenceById(productItemRequestDto.variation());

    Set<VariationOption> variationOptions =
            productItemRequestDto.variationOptions().stream()
                    .map(
                            (variationOption) -> {
                              Variation variation =
                                      variationRepository.getReferenceById(variationOption.variationId());
                              VariationOptionId variationOptionId =
                                      new VariationOptionId(variation, variationOption.value());
                              return variationOptionRepository.getReferenceById(variationOptionId);
                            })
                    .collect(Collectors.toSet());

    Product product = productRepository.getReferenceById(id);

    ProductItem productItem = new ProductItem();
    productItem.setProduct(product);
    productItem.setPrice(productItemRequestDto.price());
    productItem.setStock(productItemRequestDto.stock());
    productItem.setSku(productItemRequestDto.sku());
    productItem.setVariationOptions(variationOptions);

    return productItemModelAssembler.toModel(productItemRepository.save(productItem));
  }

  @Override
  @Transactional
  public ProductItemDto replaceVariation(
      Long id, Long productItemId, ProductItemRequestDto productItemRequestDto) {
    //    Variation variation =
    // variationRepository.getReferenceById(productItemRequestDto.variation());

    Set<VariationOption> variationOptions =
        productItemRequestDto.variationOptions().stream()
            .map(
                (variationOption) -> {
                  Variation variation =
                      variationRepository.getReferenceById(variationOption.variationId());
                  VariationOptionId variationOptionId =
                      new VariationOptionId(variation, variationOption.value());
                  return variationOptionRepository.getReferenceById(variationOptionId);
                })
            .collect(Collectors.toSet());

    Product product = productRepository.getReferenceById(id);

    ProductItem productItem =
        productItemRepository
            .findByIdAndProduct(productItemId, product)
            .orElseThrow(() -> new ProductItemNotFoundException(productItemId));

    productItem.setPrice(productItemRequestDto.price());
    productItem.setStock(productItemRequestDto.stock());
    productItem.setSku(productItemRequestDto.sku());
    productItem.setVariationOptions(variationOptions);

    return productItemModelAssembler.toModel(productItemRepository.save(productItem));
  }

  @Override
  @Transactional
  public void deleteVariation(Long id, Long variationId) {
    log.info("deleteVariation: id={}, variationId={}", id, variationId);
    Product product = productRepository.getReferenceById(id);
    ProductItem productItem =
        productItemRepository
            .findByIdAndProduct(variationId, product)
            .orElseThrow(() -> new ProductNotFoundException(id));
    productItemRepository.delete(productItem);
  }

  @Override
  public ProductItemDto getVariation(Long id, Long variationId) {
    Product product = productRepository.getReferenceById(id);
    ProductItem productItem =
        productItemRepository
            .findByIdAndProduct(variationId, product)
            .orElseThrow((() -> new ProductItemNotFoundException(variationId)));
    return productItemModelAssembler.toModel(productItem);
  }
}
