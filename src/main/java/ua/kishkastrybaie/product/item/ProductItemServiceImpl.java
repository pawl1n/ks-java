package ua.kishkastrybaie.product.item;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kishkastrybaie.product.Product;
import ua.kishkastrybaie.product.ProductNotFoundException;
import ua.kishkastrybaie.product.ProductRepository;
import ua.kishkastrybaie.variation.Variation;
import ua.kishkastrybaie.variation.VariationNotFoundException;
import ua.kishkastrybaie.variation.VariationRepository;
import ua.kishkastrybaie.variation.option.VariationOption;
import ua.kishkastrybaie.variation.option.VariationOptionId;
import ua.kishkastrybaie.variation.option.VariationOptionNotFoundException;
import ua.kishkastrybaie.variation.option.VariationOptionRepository;

@Service
@RequiredArgsConstructor
public class ProductItemServiceImpl implements ProductItemService {
  private final ProductItemRepository productItemRepository;
  private final ProductItemModelAssembler productItemModelAssembler;
  private final VariationOptionRepository variationOptionRepository;
  private final VariationRepository variationRepository;
  private final ProductRepository productRepository;
  private final PagedResourcesAssembler<ProductItem> pagedResourcesAssembler;

  @Override
  public CollectionModel<ProductItemDto> findAllByProductId(Long productId, Pageable pageable) {
    if (!productRepository.existsById(productId)) {
      throw new ProductNotFoundException(productId);
    }

    return pagedResourcesAssembler.toModel(
        productItemRepository.findAllByProductId(productId, pageable), productItemModelAssembler);
  }

  @Override
  public ProductItemDto findByProductIdAndId(Long productId, Long variationId) {
    if (!productRepository.existsById(productId)) {
      throw new ProductNotFoundException(productId);
    }

    ProductItem productItem =
            productItemRepository
                    .findByIdAndProductId(variationId, productId)
                    .orElseThrow((() -> new ProductItemNotFoundException(variationId)));
    return productItemModelAssembler.toModel(productItem);
  }

  @Override
  @Transactional
  public ProductItemDto create(Long productId, ProductItemRequestDto requestDto) {
    if (!productRepository.existsById(productId)) {
      throw new ProductNotFoundException(productId);
    }

    Set<VariationOption> variationOptions = getVariationOptions(requestDto.variationOptions());

    Product product = productRepository.getReferenceById(productId);

    ProductItem productItem = new ProductItem();
    productItem.setProduct(product);
    productItem.setStock(requestDto.stock());
    productItem.setVariationOptions(variationOptions);

    return productItemModelAssembler.toModel(productItemRepository.save(productItem));
  }

  @Override
  @Transactional
  public ProductItemDto replace(
      Long productId, Long productItemId, ProductItemRequestDto requestDto) {
    if (!productRepository.existsById(productId)) {
      throw new ProductNotFoundException(productId);
    }

    Set<VariationOption> variationOptions = getVariationOptions(requestDto.variationOptions());

    ProductItem productItem =
        productItemRepository
            .findByIdAndProductId(productItemId, productId)
            .orElseThrow(() -> new ProductItemNotFoundException(productItemId));

    productItem.setStock(requestDto.stock());
    productItem.setVariationOptions(variationOptions);

    return productItemModelAssembler.toModel(productItemRepository.save(productItem));
  }

  @Override
  @Transactional
  public void delete(Long productId, Long variationId) {
    if (!productRepository.existsById(productId)) {
      throw new ProductNotFoundException(productId);
    }

    ProductItem productItem =
        productItemRepository
            .findByIdAndProductId(variationId, productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));
    productItemRepository.delete(productItem);
  }

  private Set<VariationOption> getVariationOptions(
      Set<ProductItemRequestDto.VariationOptionRequestDto> variationOptions) {

    return variationOptions.stream()
        .map(
            variationOption -> {
              if (!variationRepository.existsById(variationOption.variationId())) {
                throw new VariationNotFoundException(variationOption.variationId());
              }

              Variation variation =
                  variationRepository.getReferenceById(variationOption.variationId());

              VariationOptionId variationOptionId =
                  new VariationOptionId(variation, variationOption.value());

              if (!variationOptionRepository.existsById(variationOptionId)) {
                throw new VariationOptionNotFoundException(variationOptionId);
              }

              return variationOptionRepository.getReferenceById(variationOptionId);
            })
        .collect(Collectors.toSet());
  }
}
