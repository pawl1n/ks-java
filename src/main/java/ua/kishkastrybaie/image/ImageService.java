package ua.kishkastrybaie.image;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;

public interface ImageService {
  CollectionModel<ImageDto> findAll(Pageable pageable);

  ImageDto findById(Long id);

  ImageDto create(ImageRequestDto imageRequestDto);

  ImageDto replace(Long id, ImageRequestDto imageRequestDto);

  void deleteById(Long id);
}
