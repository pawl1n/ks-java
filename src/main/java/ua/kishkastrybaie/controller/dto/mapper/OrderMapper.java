package ua.kishkastrybaie.controller.dto.mapper;

import org.mapstruct.Mapper;
import ua.kishkastrybaie.controller.dto.OrderDto;
import ua.kishkastrybaie.repository.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
