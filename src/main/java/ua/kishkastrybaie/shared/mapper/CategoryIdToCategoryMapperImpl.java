package ua.kishkastrybaie.shared.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.kishkastrybaie.category.CategoryNotFoundException;
import ua.kishkastrybaie.category.CategoryRepository;
import ua.kishkastrybaie.category.Category;

@Component
@RequiredArgsConstructor
public class CategoryIdToCategoryMapperImpl {
  private final CategoryRepository categoryRepository;

  @CategoryIdToCategory
  public Category toCategory(Long id) {
    if (id == null) return null;

    return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
  }
}
