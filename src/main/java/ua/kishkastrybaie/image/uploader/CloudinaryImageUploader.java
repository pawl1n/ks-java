package ua.kishkastrybaie.image.uploader;

import com.cloudinary.Cloudinary;
import com.cloudinary.EagerTransformation;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class CloudinaryImageUploader implements ImageUploader {

  private static final int IMAGE_WIDTH = 150;
  private static final int IMAGE_HEIGHT = 150;
  private static final String IMAGE_CROP = "scale";
  private static final String IMAGE_FORMAT = "webp";

  private final Cloudinary cloudinary;

  public CloudinaryImageUploader(
      @Value("${cloudinary.cloud-name}") String cloudName,
      @Value("${cloudinary.api-key}") String apiKey,
      @Value("${cloudinary.api-secret}") String apiSecret) {
    Map<String, String> config = new HashMap<>();
    config.put("cloud_name", cloudName);
    config.put("api_key", apiKey);
    config.put("api_secret", apiSecret);
    cloudinary = new Cloudinary(config);
  }

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

    File tempFile = getTempFile(base64encodedImage, filename);

    try {
      Map<?, ?> res = cloudinary.uploader().upload(tempFile, options);

      log.info("Uploaded file: {}", res.get("url"));

      Files.delete(tempFile.toPath());

      return new URL(res.get("url").toString());

    } catch (IOException exception) {
      throw new ImageUploadException(exception.getMessage());
    }
  }

  private File getTempFile(String base64encodedImage, String name) {
    byte[] decodedBytes = Base64.getDecoder().decode(base64encodedImage);

    try {
      Path tempFilePath = Files.createTempFile("", name);
      File tempFile = tempFilePath.toFile();

      Files.write(tempFilePath, decodedBytes);
      return tempFile;

    } catch (IOException exception) {
      throw new ImageUploadException(exception.getMessage());
    }
  }
}
