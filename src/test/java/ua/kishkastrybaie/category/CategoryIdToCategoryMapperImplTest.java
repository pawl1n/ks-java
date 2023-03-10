package ua.kishkastrybaie.category;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.kishkastrybaie.shared.mapper.CategoryIdToCategoryMapperImpl;

@ExtendWith(SpringExtension.class)
class CategoryIdToCategoryMapperImplTest {
  @InjectMocks private CategoryIdToCategoryMapperImpl categoryIdToCategoryMapperImpl;

  @Mock private CategoryRepository categoryRepository;

  @Test
  void shouldMapToCategory() {
    // given
    Category category = new Category();
    category.setId(1L);
    category.setName("Category");
    category.setParentCategory(new Category());

    Category category1 = new Category();
    category1.setId(2L);
    category1.setName("Category1");
    category1.setParentCategory(category);

    Category category2 = new Category();
    category2.setId(3L);
    category2.setName("Category2");
    category2.setParentCategory(category1);

    Category category3 = new Category();
    category3.setId(4L);
    category3.setName("Category3");
    category3.setParentCategory(category2);

    given(categoryRepository.findById(1L)).willReturn(Optional.of(category3));

    // when
    Category actual = categoryIdToCategoryMapperImpl.toCategory(1L);

    // then
    then(actual).isEqualTo(category3);
    verify(categoryRepository).findById(1L);
  }

  @Test
  void shouldNotMapWhenCategoryIdIsNull() {
    // given

    // when
    Category category = categoryIdToCategoryMapperImpl.toCategory(null);

    // then
    then(category).isNull();
  }

  @Test
  void shouldThrowExceptionWhenInvalidCategoryId() {
    // given

    // when
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> categoryIdToCategoryMapperImpl.toCategory(1L))
        .isInstanceOf(CategoryNotFoundException.class);
    verify(categoryRepository).findById(1L);
  }
}
