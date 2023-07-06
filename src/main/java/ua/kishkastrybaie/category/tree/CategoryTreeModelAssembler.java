package ua.kishkastrybaie.category.tree;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.kishkastrybaie.category.Category;
import ua.kishkastrybaie.category.CategoryController;
import ua.kishkastrybaie.category.CategoryMapper;

@Component
@RequiredArgsConstructor
public class CategoryTreeModelAssembler
    implements RepresentationModelAssembler<Category, CategoryTreeDto> {
  private final CategoryMapper categoryMapper;

  @Override
  @NonNull
  public CategoryTreeDto toModel(@NonNull Category category) {
    CategoryTreeDto categoryTreeDto = categoryMapper.toTreeDto(category);

    addLinks(categoryTreeDto);

    return categoryTreeDto;
  }

  @Override
  @NonNull
  public CollectionModel<CategoryTreeDto> toCollectionModel(
      @NonNull Iterable<? extends Category> categories) {
    CollectionModel<CategoryTreeDto> mapped =
        StreamSupport.stream(categories.spliterator(), false)
            .map(this::toModel)
            .collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));

    return mapped.add(linkTo(methodOn(CategoryController.class).tree()).withSelfRel());
  }

  private void addLinks(CategoryTreeDto category) {
    if (category.getDescendants() != null) {
      category.getDescendants().forEach(this::addLinks);
    }

    category.add(linkTo(methodOn(CategoryController.class).one(category.getId())).withSelfRel());
  }
}
