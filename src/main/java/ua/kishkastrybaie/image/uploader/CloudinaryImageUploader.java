package ua.kishkastrybaie.image.uploader;


import com.cloudinary.Cloudinary;
import com.cloudinary.EagerTransformation;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
@RequiredArgsConstructor
public class CloudinaryImageUploader implements ImageUploader {

  private static final int IMAGE_WIDTH = 150;
  private static final int IMAGE_HEIGHT = 150;
  private static final String IMAGE_CROP = "fill";
  private static final String IMAGE_FORMAT = "webp";

  private final Cloudinary cloudinary;

  @Override
  public URL upload(String base64encodedImage, String filename) {
    if (!StringUtils.hasText(base64encodedImage)) {
      throw new ImageUploadException("Base64 encoded image is empty");
    }
    if (!StringUtils.hasText(filename)) {
      throw new ImageUploadException("Filename is empty");
    }

    String publicId = StringUtils.replace(filename, " ", "_");
    EagerTransformation transformation =
        new EagerTransformation()
            .width(IMAGE_WIDTH)
            .height(IMAGE_HEIGHT)
            .crop(IMAGE_CROP)
            .fetchFormat(IMAGE_FORMAT);

    Map<String, Object> options = new HashMap<>();
    options.put("public_id", publicId);
    options.put("eager", List.of(transformation));

    try {
      byte[] decodedBytes = Base64.getDecoder().decode(base64encodedImage);
      Map<?, ?> res = cloudinary.uploader().upload(decodedBytes, options);

      log.info("Uploaded file: {}", res);

      if (res.get("eager") instanceof List<?> eagerMapArray) {
        for (var eagerItem : eagerMapArray) {
          if (eagerItem instanceof Map<?, ?> eagerMap
              && eagerMap.get("format") instanceof String format
              && (format.equals(IMAGE_FORMAT))) {
            return new URL(eagerMap.get("url").toString());
          }
        }
      }

      return new URL(res.get("url").toString());

    } catch (IOException exception) {
      throw new ImageUploadException(exception.getMessage());
    }
  }
}
