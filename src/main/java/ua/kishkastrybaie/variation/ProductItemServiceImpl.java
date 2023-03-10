package ua.kishkastrybaie.variation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.product.ProductItemNotFoundException;

@Service
@RequiredArgsConstructor
public class ProductItemServiceImpl implements ProductItemService {
  private final ProductItemRepository productItemRepository;

  @Override
  public List<ProductItem> findAll() {
    return productItemRepository.findAll();
  }

  @Override
  public ProductItem findById(Long id) {
    return productItemRepository.findById(id).orElseThrow(ProductItemNotFoundException::new);
  }

  @Override
  public ProductItem create(ProductItem productItem) {
    return productItemRepository.save(productItem);
  }

  @Override
  public ProductItem update(Long id, ProductItem productItemDetails) {
    ProductItem productItem =
        productItemRepository.findById(id).orElseThrow(ProductItemNotFoundException::new);

    if (productItemDetails.getSku() != null) {
      productItem.setSku(productItemDetails.getSku());
    }
    if (productItemDetails.getPrice() != null) {
      productItem.setPrice(productItemDetails.getPrice());
    }
    if (productItemDetails.getStock() != null) {
      productItem.setStock(productItemDetails.getStock());
    }

    return productItemRepository.save(productItem);
  }

  @Override
  public ProductItem replace(Long id, ProductItem productItemDetails) {
    return productItemRepository
        .findById(id)
        .map(
            productItem -> {
              productItem.setSku(productItemDetails.getSku());
              productItem.setPrice(productItemDetails.getPrice());
              productItem.setStock(productItemDetails.getStock());
              return productItemRepository.save(productItem);
            })
        .orElseGet(
            () -> {
              productItemDetails.setId(id);
              return productItemRepository.save(productItemDetails);
            });
  }

  @Override
  public void deleteById(Long id) {
    ProductItem productItem =
        productItemRepository.findById(id).orElseThrow(ProductItemNotFoundException::new);

    productItemRepository.delete(productItem);
  }
}
