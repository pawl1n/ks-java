package ua.kishkastrybaie.image;

import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
  private final ImageRepository imageRepository;
  private final ImageModelAssembler imageModelAssembler;
  private final ImageUploader imageUploader;

  @Override
  public CollectionModel<ImageDto> findAll() {
    return imageModelAssembler.toCollectionModel(imageRepository.findAll());
  }

  @Override
  public ImageDto findById(Long id) {
    return imageModelAssembler.toModel(
        imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException(id)));
  }

  @Override
  public ImageDto create(ImageRequestDto imageRequestDto) {
    Image image = new Image();
    image.setName(imageRequestDto.name());
    image.setDescription(imageRequestDto.description());

    URL url = imageUploader.upload(imageRequestDto.base64Image(), imageRequestDto.name());

    image.setUrl(url);

    return imageModelAssembler.toModel(imageRepository.save(image));
  }

  @Override
  public ImageDto replace(Long id, ImageRequestDto imageRequestDto) {
    return null;
  }

  @Override
  public void deleteById(Long id) {
    Image image = imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException(id));
    imageRepository.delete(image);
  }
}
