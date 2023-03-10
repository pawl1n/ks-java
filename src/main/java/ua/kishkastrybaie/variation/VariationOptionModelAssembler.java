package ua.kishkastrybaie.variation;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VariationOptionModelAssembler
    implements RepresentationModelAssembler<VariationOption, VariationOptionDto> {
  private final VariationOptionMapper variationOptionMapper;

  @Override
  @NonNull
  public VariationOptionDto toModel(@NonNull VariationOption variationOption) {
    return variationOptionMapper
        .toDto(variationOption)
        .add(
            linkTo(methodOn(VariationOptionController.class).one(variationOption.getId()))
                .withSelfRel());
  }

  @Override
  @NonNull
  public CollectionModel<VariationOptionDto> toCollectionModel(
      @NonNull Iterable<? extends VariationOption> variationOptions) {
    return RepresentationModelAssembler.super
        .toCollectionModel(variationOptions)
        .add(linkTo(methodOn(VariationOptionController.class).all()).withSelfRel());
  }
}
