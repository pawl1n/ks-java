package ua.kishkastrybaie.product.item;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.kishkastrybaie.product.ProductController;

@Component
@RequiredArgsConstructor
public class ProductItemModelAssembler
    implements RepresentationModelAssembler<ProductItem, ProductItemDto> {
  private final ProductItemMapper productItemMapper;
  private final HttpServletRequest request;

  @Override
  @NonNull
  public ProductItemDto toModel(@NonNull ProductItem productItem) {
    return productItemMapper
        .toDto(productItem)
        .add(
            linkTo(
                    methodOn(ProductController.class)
                        .variation(productItem.getProduct().getId(), productItem.getId()))
                .withSelfRel());
  }

  @Override
  @NonNull
  public CollectionModel<ProductItemDto> toCollectionModel(
      @NonNull Iterable<? extends ProductItem> productItems) {
    return RepresentationModelAssembler.super
        .toCollectionModel(productItems)
        .add(Link.of(request.getRequestURL().toString()).withSelfRel());
  }
}
