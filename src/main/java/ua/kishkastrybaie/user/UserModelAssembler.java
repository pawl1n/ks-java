package ua.kishkastrybaie.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserModelAssembler implements RepresentationModelAssembler<User, UserDto> {
  private final UserMapper userMapper;

  @Override
  @NonNull
  public UserDto toModel(@NonNull User user) {
    return userMapper
        .toDto(user)
        .add(linkTo(methodOn(UserController.class).getCurrentUser()).withSelfRel())
        .add(
            linkTo(methodOn(UserController.class).changePassword(null)).withRel("change-password"));
  }
}
