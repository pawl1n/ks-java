package ua.kishkastrybaie.image.uploader;

import com.cloudinary.Cloudinary;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
@EnableConfigurationProperties(CloudinaryImageUploaderProperties.class)
public class CloudinaryConfiguration {
    @Bean
    public Cloudinary cloudinary(CloudinaryImageUploaderProperties properties) {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", properties.getCloudName());
        config.put("api_key", properties.getApiKey());
        config.put("api_secret", properties.getApiSecret());
        return new Cloudinary(config);
    }
}
