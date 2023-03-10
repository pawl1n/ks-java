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
import org.springframework.hateoas.CollectionModel;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
  private static Category category1;
  private static Category category2;
  private static CategoryDto categoryDto1;
  private static CategoryDto categoryDto2;
  private static CategoryRequestDto categoryRequestDto;
  @Mock private CategoryMapper categoryMapper;
  @Mock private CategoryModelAssembler categoryModelAssembler;
  @Mock private CategoryRepository categoryRepository;
  @InjectMocks private CategoryServiceImpl categoryService;

  @BeforeEach
  void setUp() {
    category1 = new Category();
    category1.setId(1L);
    category1.setName("Parent category");
    category1.setParentCategory(null);

    category2 = new Category();
    category2.setId(2L);
    category2.setName("Name");
    category2.setParentCategory(category1);

    categoryDto1 = new CategoryDto(1L, "Parent category", null);

    categoryDto2 = new CategoryDto(2L, "Name", "Parent category");

    categoryRequestDto = new CategoryRequestDto("Parent category", null);
  }

  @Test
  void shouldFindAll() {
    // given
    List<Category> categories = List.of(category1, category2);

    CollectionModel<CategoryDto> categoryDtoCollectionModel =
        CollectionModel.of(List.of(categoryDto1, categoryDto2));
    given(categoryRepository.findAll()).willReturn(categories);
    given(categoryModelAssembler.toCollectionModel(categories))
        .willReturn(categoryDtoCollectionModel);

    // when
    CollectionModel<CategoryDto> actual = categoryService.findAll();

    // then
    then(actual).isNotNull().isEqualTo(categoryDtoCollectionModel);
    verify(categoryRepository).findAll();
    verify(categoryModelAssembler).toCollectionModel(categories);
  }

  @Test
  void shouldFindById() {
    // given
    CategoryDto categoryDto = new CategoryDto(2L, "Name", "Parent category");

    given(categoryRepository.findById(any())).willReturn(Optional.of(category2));
    given(categoryModelAssembler.toModel(any())).willReturn(categoryDto);

    // when
    CategoryDto actual = categoryService.findById(1L);

    // then
    then(actual).isNotNull().isEqualTo(categoryDto);
    verify(categoryRepository).findById(any());
    verify(categoryModelAssembler).toModel(any());
  }

  @Test
  void shouldThrowExceptionWhenInvalidParentCategoryId() {
    // given

    // when
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> categoryService.findById(1L)).isInstanceOf(CategoryNotFoundException.class);
    verify(categoryRepository).findById(1L);
    verifyNoInteractions(categoryModelAssembler);
  }

  @Test
  void shouldCreate() {
    // given
    given(categoryMapper.toDomain(categoryRequestDto)).willReturn(category1);
    given(categoryRepository.save(category1)).willReturn(category1);
    given(categoryModelAssembler.toModel(category1)).willReturn(categoryDto1);

    // when
    CategoryDto actual = categoryService.create(categoryRequestDto);

    // then
    then(actual).isNotNull().isEqualTo(categoryDto1);
    verify(categoryMapper).toDomain(categoryRequestDto);
    verify(categoryRepository).save(category1);
    verify(categoryModelAssembler).toModel(category1);
  }

  @Test
  void shouldReplace() {
    // given
    Category changedCategory = new Category();
    changedCategory.setId(2L);
    changedCategory.setName("Parent category");
    changedCategory.setParentCategory(null);

    CategoryDto changedCategoryDto = new CategoryDto(2L, "New name", null);

    given(categoryMapper.toDomain(categoryRequestDto)).willReturn(category1);
    given(categoryRepository.findById(2L)).willReturn(Optional.of(category2));
    given(categoryRepository.save(changedCategory)).willReturn(changedCategory);
    given(categoryModelAssembler.toModel(changedCategory)).willReturn(changedCategoryDto);

    // when
    CategoryDto actual = categoryService.replace(2L, categoryRequestDto);

    // then
    then(actual).isEqualTo(changedCategoryDto);
    verify(categoryMapper).toDomain(categoryRequestDto);
    verify(categoryRepository).findById(2L);
    verify(categoryRepository).save(changedCategory);
    verify(categoryModelAssembler).toModel(changedCategory);
  }

  @Test
  void shouldNotReplaceWhenInvalidId() {
    // given
    given(categoryMapper.toDomain(categoryRequestDto)).willReturn(category1);

    // when
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> categoryService.replace(1L, categoryRequestDto))
        .isInstanceOf(CategoryNotFoundException.class);
    verify(categoryMapper).toDomain(categoryRequestDto);
    verify(categoryRepository).findById(1L);
    verifyNoInteractions(categoryModelAssembler);
  }

  @Test
  void shouldDeleteById() {
    // given
    given(categoryRepository.findById(1L)).willReturn(Optional.of(category1));

    // when
    categoryService.deleteById(1L);

    // then
    verify(categoryRepository).findById(1L);
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
    verify(categoryRepository).findById(1L);
    verifyNoInteractions(categoryModelAssembler);
  }

  @Test
  void shouldGetParentCategory() {
    // given
    given(categoryRepository.findById(2L)).willReturn(Optional.of(category2));
    given(categoryModelAssembler.toModel(category1)).willReturn(categoryDto1);

    // when
    CategoryDto actual = categoryService.getParentCategory(2L);

    // then
    then(actual).isNotNull().isEqualTo(categoryDto1);
    verify(categoryRepository).findById(2L);
    verify(categoryModelAssembler).toModel(category1);
  }

  @Test
  void shouldNotGetParentCategoryWhenInvalidId() {
    // given

    // when
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> categoryService.getParentCategory(1L))
        .isInstanceOf(CategoryNotFoundException.class);
    verify(categoryRepository).findById(1L);
    verifyNoInteractions(categoryModelAssembler);
  }

  @Test
  void shouldNotGetParentCategoryWhenNoParentCategory() {
    // given

    // when
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

    // then
    thenThrownBy(() -> categoryService.getParentCategory(1L))
        .isInstanceOf(CategoryNotFoundException.class);
    verify(categoryRepository).findById(1L);
    verifyNoInteractions(categoryModelAssembler);
  }
}
