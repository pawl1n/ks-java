package ua.kishkastrybaie.variation.option;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.kishkastrybaie.variation.VariationController;

@Component
@RequiredArgsConstructor
public class VariationOptionModelAssembler
    implements RepresentationModelAssembler<VariationOption, VariationOptionDto> {
  private final VariationOptionMapper variationOptionMapper;
  private final HttpServletRequest request;

  @Override
  @NonNull
  public VariationOptionDto toModel(@NonNull VariationOption variationOption) {
    return variationOptionMapper
        .toDto(variationOption)
        .add(
            linkTo(
                    methodOn(VariationController.class)
                        .option(variationOption.getVariation().getId(), variationOption.getValue()))
                .withSelfRel());
  }

  @Override
  @NonNull
  public CollectionModel<VariationOptionDto> toCollectionModel(
      @NonNull Iterable<? extends VariationOption> variationOptions) {
    return RepresentationModelAssembler.super
        .toCollectionModel(variationOptions)
        .add(Link.of(request.getRequestURL().toString()).withSelfRel());
  }
}
