package ua.kishkastrybaie.image.uploader;

import com.cloudinary.Cloudinary;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class CloudinaryConfiguration {
  @Bean
  public Cloudinary cloudinary() {
    return new Cloudinary();
  }
}
