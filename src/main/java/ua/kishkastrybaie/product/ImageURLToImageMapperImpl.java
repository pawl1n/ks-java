package ua.kishkastrybaie.product;

import java.net.URL;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.kishkastrybaie.image.Image;
import ua.kishkastrybaie.image.ImageRepository;

@Component
@RequiredArgsConstructor
class ImageURLToImageMapperImpl {
  private final ImageRepository imageRepository;

  @ImageURLToImage
  Image toImage(URL imageUrl) {
    if (imageUrl == null) {
      return null;
    }

    return imageRepository
        .findImageByUrl(imageUrl)
        .orElseGet(
            () -> {
              Image image = new Image();
              String filename = Paths.get(imageUrl.getPath()).getFileName().toString();
              String filenameWithoutExtension = filename.substring(0, filename.lastIndexOf('.'));
              image.setName(filenameWithoutExtension);
              image.setUrl(imageUrl);
              return image;
            });
  }
}
