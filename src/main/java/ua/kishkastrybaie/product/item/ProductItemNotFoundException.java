package ua.kishkastrybaie.product.item;

public class ProductItemNotFoundException extends RuntimeException {

  public ProductItemNotFoundException(Long id) {
    super("Product item with id " + id + " not found");
  }
}
