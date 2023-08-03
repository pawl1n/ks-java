package ua.kishkastrybaie.product.details;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.kishkastrybaie.product.Product;
import ua.kishkastrybaie.product.ProductController;
import ua.kishkastrybaie.product.ProductMapper;

@Component
@RequiredArgsConstructor
public class ProductDetailsModelAssembler
    implements RepresentationModelAssembler<Product, ProductDetailsDto> {
  private final ProductMapper productMapper;

  @Override
  @NonNull
  public ProductDetailsDto toModel(@NonNull Product product) {
    return productMapper
        .toDetailsDto(product)
        .add(linkTo(methodOn(ProductController.class).details(product.getSlug())).withSelfRel());
  }

  @Override
  @NonNull
  public CollectionModel<ProductDetailsDto> toCollectionModel(
      @NonNull Iterable<? extends Product> products) {
    return RepresentationModelAssembler.super.toCollectionModel(products);
  }
}
