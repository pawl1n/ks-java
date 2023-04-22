package ua.kishkastrybaie.image;

import java.net.URL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
  boolean existsByUrl(URL url);

  boolean existsByName(String name);

  Image getReferenceByUrl(URL url);
}
