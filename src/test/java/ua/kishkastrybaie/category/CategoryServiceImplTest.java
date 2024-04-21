package ua.kishkastrybaie.category;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
  private static Category category1;
  private static Category category2;
  private static CategoryDto categoryDto1;
  private static CategoryDto categoryDto2;
  private static CategoryRequestDto categoryRequestDto;
  @Mock
  private CategoryModelAssembler categoryModelAssembler;
  @Mock
  private CategoryRepository categoryRepository;
  @Mock
  private PagedResourcesAssembler<Category> pagedResourcesAssembler;
  @InjectMocks
  private CategoryServiceImpl categoryService;

  @BeforeEach
  void setUp() {
    category1 = new Category();
    category1.setId(1L);
    category1.setName("Parent category");

    category2 = new Category();
    category2.setId(2L);
    category2.setName("Name");
    category2.setParentCategory(category1);

    categoryDto1 = new CategoryDto(1L, "Parent category", "/", null, null);

    categoryDto2 = new CategoryDto(2L, "Name", "/parent-category/", 1L, null);

    categoryRequestDto = new CategoryRequestDto("Parent category", 1L, null, null);
  }

  @Test
  void shouldFindAll() {
    // given
    Page<Category> categories = new PageImpl<>(List.of(category1, category2));
    PagedModel<CategoryDto> categoryDtoCollectionModel = PagedModel.of(List.of(categoryDto1, categoryDto2),
        new PagedModel.PageMetadata(5, 0, 1));

    given(categoryRepository.findAll(PageRequest.ofSize(5))).willReturn(categories);
    given(pagedResourcesAssembler.toModel(categories, categoryModelAssembler))
        .willReturn(categoryDtoCollectionModel);

    // when
    CollectionModel<CategoryDto> actual = categoryService.findAll(PageRequest.ofSize(5));

    // then
    then(actual).isNotNull().isEqualTo(categoryDtoCollectionModel);
  }

  @Test
  void shouldFindRootCategories() {
    // given
    List<Category> categories = List.of(category1);
    CollectionModel<CategoryDto> categoryDtoCollectionModel = CollectionModel.of(List.of(categoryDto1));

    given(categoryRepository.findRootCategories()).willReturn(categories);
    given(categoryModelAssembler.toCollectionModel(categories))
        .willReturn(categoryDtoCollectionModel);

    // when
    CollectionModel<CategoryDto> actual = categoryService.findRootCategories();

    // then
    then(actual).isNotNull().isEqualTo(categoryDtoCollectionModel);
  }

  @Test
  void shouldFindById() {
    // given
    CategoryDto categoryDto = new CategoryDto(2L, "Name", "parent-category", 1L, null);

    given(categoryRepository.findById(any())).willReturn(Optional.of(category2));
    given(categoryModelAssembler.toModel(any())).willReturn(categoryDto);

    // when
    CategoryDto actual = categoryService.findById(1L);

    // then
    then(actual).isNotNull().isEqualTo(categoryDto);
  }

  @Test
  void shouldNotFindByIdWhenInvalidId() {
    // given

    // when
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> categoryService.findById(1L)).isInstanceOf(CategoryNotFoundException.class);
    verifyNoInteractions(categoryModelAssembler);
  }

  @Test
  void shouldCreate() {
    // given
    given(categoryRepository.existsById(1L)).willReturn(true);
    given(categoryRepository.getReferenceById(1L)).willReturn(category1);
    given(
        categoryRepository.save(
            argThat(category -> category.getName().equals(categoryRequestDto.name()))))
        .willReturn(category2);
    given(categoryModelAssembler.toModel(category2)).willReturn(categoryDto2);

    // when
    CategoryDto actual = categoryService.create(categoryRequestDto);

    // then
    then(actual)
        .isNotNull()
        .usingRecursiveComparison()
        .ignoringFields("path")
        .isEqualTo(categoryDto2);
  }

  @Test
  void shouldReplace() {
    // given
    Category changedCategory = new Category();
    changedCategory.setId(2L);
    changedCategory.setName("Parent category");
    // changedCategory.setPath("/1/");

    CategoryDto changedCategoryDto = new CategoryDto(2L, "New name", "/parent-category/", 1L, null);

    given(categoryRepository.existsById(1L)).willReturn(true);
    given(categoryRepository.getReferenceById(1L)).willReturn(category1);
    given(categoryRepository.findById(2L)).willReturn(Optional.of(category2));
    given(
        categoryRepository.save(
            argThat(
                category -> category.getId().equals(2L)
                    && category.getName().equals(categoryRequestDto.name()))))
        .willReturn(changedCategory);
    given(categoryModelAssembler.toModel(changedCategory)).willReturn(changedCategoryDto);

    // when
    CategoryDto actual = categoryService.replace(2L, categoryRequestDto);

    // then
    then(actual).isEqualTo(changedCategoryDto);
  }

  @Test
  void shouldNotReplaceWhenInvalidId() {
    // given
    given(categoryRepository.existsById(1L)).willReturn(true);
    given(categoryRepository.getReferenceById(1L)).willReturn(category1);

    // when
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> categoryService.replace(1L, categoryRequestDto))
        .isInstanceOf(CategoryNotFoundException.class);
    verifyNoInteractions(categoryModelAssembler);
  }

  @Test
  void shouldDeleteById() {
    // given
    given(categoryRepository.findById(1L)).willReturn(Optional.of(category1));

    // when
    categoryService.deleteById(1L);

    // then
    verify(categoryRepository).delete(category1);
  }

  @Test
  void shouldNotDeleteByIdWhenInvalidId() {
    // given

    // when
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> categoryService.deleteById(1L))
        .isInstanceOf(CategoryNotFoundException.class);
    verifyNoInteractions(categoryModelAssembler);
  }

  @Test
  void shouldFindAllByParentCategoryId() {
    // given
    List<Category> categories = List.of(category2);
    CollectionModel<CategoryDto> categoryDtoCollectionModel = CollectionModel.of(List.of(categoryDto2));

    given(categoryRepository.findAllDescendantsById(1L)).willReturn(categories);
    given(categoryModelAssembler.toCollectionModel(categories))
        .willReturn(categoryDtoCollectionModel);

    // when
    CollectionModel<CategoryDto> actual = categoryService.findAllDescendants(1L);

    // then
    then(actual).isNotNull().isEqualTo(categoryDtoCollectionModel);
  }
}
