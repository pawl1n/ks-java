package ua.kishkastrybaie.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(itemRelation = "user", collectionRelation = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto extends RepresentationModel<UserDto> {
  private final String firstName;
  private final String middleName;
  private final String lastName;
  private final String email;
  private final String phoneNumber;
  private final Role role;
}
