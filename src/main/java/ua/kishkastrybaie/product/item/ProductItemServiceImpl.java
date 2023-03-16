package ua.kishkastrybaie.product.item;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kishkastrybaie.product.Product;
import ua.kishkastrybaie.product.ProductNotFoundException;
import ua.kishkastrybaie.product.ProductRepository;
import ua.kishkastrybaie.variation.Variation;
import ua.kishkastrybaie.variation.VariationRepository;
import ua.kishkastrybaie.variation.option.VariationOption;
import ua.kishkastrybaie.variation.option.VariationOptionId;
import ua.kishkastrybaie.variation.option.VariationOptionRepository;

@Service
@RequiredArgsConstructor
public class ProductItemServiceImpl implements ProductItemService {
  private final ProductItemRepository productItemRepository;
  private final RepresentationModelAssembler<ProductItem, ProductItemDto> productItemModelAssembler;
  private final VariationOptionRepository variationOptionRepository;
  private final VariationRepository variationRepository;
  private final ProductRepository productRepository;

  @Override
  public CollectionModel<ProductItemDto> getProductVariations(Long productId) {
    return productItemModelAssembler.toCollectionModel(
        productItemRepository.findAllByProductId(productId));
  }

  @Override
  @Transactional
  public ProductItemDto addVariation(Long productId, ProductItemRequestDto productItemRequestDto) {

    Set<VariationOption> variationOptions =
        getVariationOptions(productItemRequestDto.variationOptions());

    Product product = productRepository.getReferenceById(productId);

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
      Long productId, Long productItemId, ProductItemRequestDto productItemRequestDto) {

    Set<VariationOption> variationOptions =
        getVariationOptions(productItemRequestDto.variationOptions());

    Product product = productRepository.getReferenceById(productId);

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
  public void deleteVariation(Long productId, Long variationId) {
    Product product = productRepository.getReferenceById(productId);
    ProductItem productItem =
        productItemRepository
            .findByIdAndProduct(variationId, product)
            .orElseThrow(() -> new ProductNotFoundException(productId));
    productItemRepository.delete(productItem);
  }

  @Override
  public ProductItemDto getVariation(Long productId, Long variationId) {
    Product product = productRepository.getReferenceById(productId);
    ProductItem productItem =
        productItemRepository
            .findByIdAndProduct(variationId, product)
            .orElseThrow((() -> new ProductItemNotFoundException(variationId)));
    return productItemModelAssembler.toModel(productItem);
  }

  private Set<VariationOption> getVariationOptions(
      Set<ProductItemRequestDto.VariationOptionRequestDto> variationOptions) {

    return variationOptions.stream()
        .map(
            variationOption -> {
              Variation variation =
                  variationRepository.getReferenceById(variationOption.variationId());
              VariationOptionId variationOptionId =
                  new VariationOptionId(variation, variationOption.value());
              return variationOptionRepository.getReferenceById(variationOptionId);
            })
        .collect(Collectors.toSet());
  }
}
