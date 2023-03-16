package ua.kishkastrybaie.product;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.kishkastrybaie.product.item.ProductItemController;

@Component
@RequiredArgsConstructor
public class ProductModelAssembler implements RepresentationModelAssembler<Product, ProductDto> {
  private final ProductMapper productMapper;

  @Override
  @NonNull
  public ProductDto toModel(@NonNull Product product) {
    return productMapper
        .toDto(product)
        .add(linkTo(methodOn(ProductController.class).one(product.getId())).withSelfRel())
        .add(
            linkTo(methodOn(ProductController.class).category(product.getId())).withRel("category"))
        .add(linkTo(methodOn(ProductController.class).image(product.getId())).withRel("mainImage"))
        .add(
            linkTo(methodOn(ProductItemController.class).all(product.getId()))
                .withRel("variations"));
  }

  @Override
  @NonNull
  public CollectionModel<ProductDto> toCollectionModel(
      @NonNull Iterable<? extends Product> products) {
    return RepresentationModelAssembler.super
        .toCollectionModel(products)
        .add(linkTo(methodOn(ProductController.class).all(null)).withSelfRel());
  }
}
