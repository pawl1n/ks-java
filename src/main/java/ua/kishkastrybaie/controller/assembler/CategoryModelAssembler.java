package ua.kishkastrybaie.controller.assembler;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.kishkastrybaie.controller.CategoryController;
import ua.kishkastrybaie.controller.dto.CategoryDto;
import ua.kishkastrybaie.controller.dto.mapper.CategoryMapper;
import ua.kishkastrybaie.repository.entity.Category;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class CategoryModelAssembler implements RepresentationModelAssembler<Category, CategoryDto> {
    private final CategoryMapper categoryMapper;

    @Override
    @NonNull
    public CategoryDto toModel(@NonNull Category category) {
        return categoryMapper.toDto(category)
                .add(linkTo(methodOn(CategoryController.class).one(category.getId())).withSelfRel())
                .add(linkTo(methodOn(CategoryController.class).parentCategory(category.getId())).withRel("parentCategory"));
    }

    @Override
    @NonNull
    public CollectionModel<CategoryDto> toCollectionModel(@NonNull Iterable<? extends Category> categories) {
        return RepresentationModelAssembler.super.toCollectionModel(categories)
                .add(linkTo(methodOn(CategoryController.class).all()).withSelfRel());
    }
}