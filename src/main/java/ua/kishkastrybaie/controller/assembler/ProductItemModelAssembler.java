package ua.kishkastrybaie.controller.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.kishkastrybaie.controller.ProductItemController;
import ua.kishkastrybaie.controller.dto.ProductItemDto;
import ua.kishkastrybaie.controller.dto.mapper.ProductItemMapper;
import ua.kishkastrybaie.repository.entity.ProductItem;

@Component
@RequiredArgsConstructor
public class ProductItemModelAssembler
    implements RepresentationModelAssembler<ProductItem, ProductItemDto> {
  private final ProductItemMapper productItemMapper;

  @Override
  @NonNull
  public ProductItemDto toModel(@NonNull ProductItem productItem) {
    return productItemMapper
        .toDto(productItem)
        .add(linkTo(methodOn(ProductItemController.class).one(productItem.getId())).withSelfRel());
  }

  @Override
  @NonNull
  public CollectionModel<ProductItemDto> toCollectionModel(
      @NonNull Iterable<? extends ProductItem> productItems) {
    return RepresentationModelAssembler.super
        .toCollectionModel(productItems)
        .add(linkTo(methodOn(ProductItemController.class).all()).withSelfRel());
  }
}
