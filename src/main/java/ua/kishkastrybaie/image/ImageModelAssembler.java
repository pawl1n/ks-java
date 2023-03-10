package ua.kishkastrybaie.image;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageModelAssembler implements RepresentationModelAssembler<Image, ImageDto> {

  @Override
  @NonNull
  public ImageDto toModel(@NonNull Image image) {
    ImageDto imageDto =
        new ImageDto(image.getId(), image.getName(), image.getDescription(), image.getUrl());

    return imageDto.add(
        WebMvcLinkBuilder.linkTo(methodOn(ImageController.class).one(image.getId())).withSelfRel());
  }

  @Override
  @NonNull
  public CollectionModel<ImageDto> toCollectionModel(@NonNull Iterable<? extends Image> images) {
    return RepresentationModelAssembler.super
        .toCollectionModel(images)
        .add(linkTo(methodOn(ImageController.class).all()).withSelfRel());
  }
}
