package ua.kishkastrybaie.image;

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
  public URL upload(String base64encodedImage, String name) {
    try {
      String publicId = StringUtils.replace(name, " ", "_");
      EagerTransformation transformation =
          new EagerTransformation().width(150).height(150).crop("scale").fetchFormat("webp");

      Map<String, Object> options = new HashMap<>();
      options.put("public_id", publicId);
      options.put("eager", List.of(transformation));

      File tempFile = getTempFile(base64encodedImage, name);

      Map<?, ?> res = cloudinary.uploader().upload(tempFile, options);

      log.info("Uploaded file: {}", res.get("secure_url"));

      return new URL(res.get("url").toString());

    } catch (IOException exception) {
      throw new ImageUploadException(exception.getMessage());
    }
  }

  private File getTempFile(String base64encodedImage, String name) throws IOException {
    byte[] decodedBytes = Base64.getDecoder().decode(base64encodedImage);

    Path tempFilePath = Files.createTempFile("", name);
    File tempFile = tempFilePath.toFile();

    Files.write(tempFilePath, decodedBytes);

    return tempFile;
  }
}
