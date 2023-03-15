package ua.kishkastrybaie.user;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto toDto(User product);
}
