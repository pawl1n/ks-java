package ua.kishkastrybaie.category;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CyclicCategoryPathException extends RuntimeException {
  public CyclicCategoryPathException(Long parentId, Long descendantId) {
    super(
        String.format(
            "Cyclic path found: Category with id %s is descendant of category with id %s",
            descendantId, parentId));
    log.error(getMessage());
  }
}
