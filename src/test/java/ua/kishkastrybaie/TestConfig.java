package ua.kishkastrybaie;

import static org.mockito.Mockito.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ua.kishkastrybaie.image.uploader.CloudinaryImageUploader;

@TestConfiguration
public class TestConfig {

  @Bean
  public CloudinaryImageUploader cloudinaryImageUploader() {
    return mock(CloudinaryImageUploader.class);
  }
}
