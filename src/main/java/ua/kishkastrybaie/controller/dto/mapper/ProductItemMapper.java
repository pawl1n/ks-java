package ua.kishkastrybaie.controller.dto.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import ua.kishkastrybaie.controller.dto.ProductItemDto;
import ua.kishkastrybaie.repository.entity.ProductItem;

@Mapper(componentModel = "spring")
public interface ProductItemMapper {
  ProductItemDto toDto(ProductItem productItem);

  List<ProductItemDto> toDto(List<ProductItem> productItems);

  ProductItem toDomain(ProductItemDto productItemDto);
}
