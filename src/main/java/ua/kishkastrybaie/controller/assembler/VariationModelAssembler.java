package ua.kishkastrybaie.controller.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.kishkastrybaie.controller.VariationController;
import ua.kishkastrybaie.controller.dto.VariationDto;
import ua.kishkastrybaie.controller.dto.mapper.VariationMapper;
import ua.kishkastrybaie.repository.entity.Variation;

@Component
@RequiredArgsConstructor
public class VariationModelAssembler
    implements RepresentationModelAssembler<Variation, VariationDto> {
  private final VariationMapper variationMapper;

  @Override
  @NonNull
  public VariationDto toModel(@NonNull Variation variation) {
    return variationMapper
        .toDto(variation)
        .add(linkTo(methodOn(VariationController.class).one(variation.getId())).withSelfRel())
        .add(
            linkTo(methodOn(VariationController.class).category(variation.getId()))
                .withRel("category"));
  }

  @Override
  @NonNull
  public CollectionModel<VariationDto> toCollectionModel(
      @NonNull Iterable<? extends Variation> variations) {
    return RepresentationModelAssembler.super
        .toCollectionModel(variations)
        .add(linkTo(methodOn(VariationController.class).all()).withSelfRel());
  }
}
