package ua.kishkastrybaie.image;

import org.springframework.hateoas.CollectionModel;

public interface ImageService {
  CollectionModel<ImageDto> findAll();

  ImageDto findById(Long id);

  ImageDto create(ImageRequestDto imageRequestDto);

  ImageDto replace(Long id, ImageRequestDto imageRequestDto);

  void deleteById(Long id);
}
