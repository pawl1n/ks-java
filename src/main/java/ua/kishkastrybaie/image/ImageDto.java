package ua.kishkastrybaie.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.net.URL;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Relation(itemRelation = "image", collectionRelation = "images")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageDto extends RepresentationModel<ImageDto> {
  private final Long id;
  private final String name;
  private final String description;
  private final URL url;
}
