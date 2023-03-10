package ua.kishkastrybaie.image;

import jakarta.persistence.*;
import java.net.URL;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "image", schema = "main")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Image {
  @Id
  @GeneratedValue(generator = "image_seq")
  @SequenceGenerator(
      name = "image_seq",
      sequenceName = "image_seq",
      schema = "main",
      allocationSize = 1)
  private Long id;

  private String name;

  private String description;

  private URL url;
}
