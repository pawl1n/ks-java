package ua.kishkastrybaie.image.uploader;

import static java.nio.file.attribute.PosixFilePermission.OWNER_READ;
import static java.nio.file.attribute.PosixFilePermission.OWNER_WRITE;

import com.cloudinary.Cloudinary;
import com.cloudinary.EagerTransformation;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
@EnableConfigurationProperties(CloudinaryImageUploaderProperties.class)
public class CloudinaryImageUploader implements ImageUploader {

  private static final int IMAGE_WIDTH = 150;
  private static final int IMAGE_HEIGHT = 150;
  private static final String IMAGE_CROP = "fill";
  private static final String IMAGE_FORMAT = "webp";

  private final Cloudinary cloudinary;

  public CloudinaryImageUploader(CloudinaryImageUploaderProperties properties) {
    Map<String, String> config = new HashMap<>();
    config.put("cloud_name", properties.getCloudName());
    config.put("api_key", properties.getApiKey());
    config.put("api_secret", properties.getApiSecret());
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

      log.info("Uploaded file: {}", res);

      Files.delete(tempFile.toPath());

      if (res.get("eager") instanceof ArrayList<?> eagerMapArray) {
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

  private File getTempFile(String base64encodedImage, String name) {
    byte[] decodedBytes = Base64.getDecoder().decode(base64encodedImage);

    try {
      FileAttribute<Set<PosixFilePermission>> permissions =
          PosixFilePermissions.asFileAttribute(EnumSet.of(OWNER_READ, OWNER_WRITE));

      Path tempFilePath = Files.createTempFile("", name, permissions);
      File tempFile = tempFilePath.toFile();

      Files.write(tempFilePath, decodedBytes);
      return tempFile;

    } catch (IOException exception) {
      throw new ImageUploadException(exception.getMessage());
    }
  }
}
