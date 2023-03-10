package ua.kishkastrybaie.variation;

import java.util.List;
import org.mapstruct.Mapper;
import ua.kishkastrybaie.product.ProductItemDto;

@Mapper(componentModel = "spring")
public interface ProductItemMapper {
  ProductItemDto toDto(ProductItem productItem);

  List<ProductItemDto> toDto(List<ProductItem> productItems);

  ProductItem toDomain(ProductItemDto productItemDto);
}
