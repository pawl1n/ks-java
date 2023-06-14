package ua.kishkastrybaie.category;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryModelAssembler implements RepresentationModelAssembler<Category, CategoryDto> {
  private final CategoryMapper categoryMapper;

  @Override
  @NonNull
  public CategoryDto toModel(@NonNull Category category) {
    return categoryMapper
        .toDto(category)
        .add(linkTo(methodOn(CategoryController.class).one(category.getId())).withSelfRel())
        .add(
            linkTo(methodOn(CategoryController.class).children(category.getId()))
                .withRel("children"));
  }

  @Override
  @NonNull
  public CollectionModel<CategoryDto> toCollectionModel(
      @NonNull Iterable<? extends Category> categories) {
    return RepresentationModelAssembler.super
        .toCollectionModel(categories)
        .add(linkTo(methodOn(CategoryController.class).all(null)).withSelfRel());
  }
}
