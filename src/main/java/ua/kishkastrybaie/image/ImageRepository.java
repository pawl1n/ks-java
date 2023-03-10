package ua.kishkastrybaie.image;

import java.net.URL;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
  Optional<Image> findImageByUrl(URL url);
  boolean existsByName(String name);
}
