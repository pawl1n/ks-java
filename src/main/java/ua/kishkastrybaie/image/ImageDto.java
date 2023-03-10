package ua.kishkastrybaie.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.net.URL;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Relation(itemRelation = "image", collectionRelation = "images")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class ImageDto extends RepresentationModel<ImageDto> {
  private Long id;
  private String name;
  private String description;
  private URL url;
}
