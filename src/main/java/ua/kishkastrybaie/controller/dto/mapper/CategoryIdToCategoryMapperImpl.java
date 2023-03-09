package ua.kishkastrybaie.controller.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.kishkastrybaie.exception.CategoryNotFoundException;
import ua.kishkastrybaie.repository.CategoryRepository;
import ua.kishkastrybaie.repository.entity.Category;

@Component
@RequiredArgsConstructor
class CategoryIdToCategoryMapperImpl {
  private final CategoryRepository categoryRepository;

  @CategoryIdToCategory
  Category toCategory(Long id) {
    if (id == null) return null;

    return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
  }
}
