package ua.kishkastrybaie.product;

import org.mapstruct.*;
import ua.kishkastrybaie.category.CategoryMapper;

@Mapper(
    componentModel = "spring",
    uses = {
      CategoryMapper.class
    },
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductMapper {

  @Mapping(source = "mainImage.url", target = "mainImage")
  ProductDto toDto(Product product);
}
