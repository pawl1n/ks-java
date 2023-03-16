package ua.kishkastrybaie.image.uploader;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloudinary")
@Getter
@RequiredArgsConstructor
public class CloudinaryImageUploaderProperties {
    private final String cloudName;
    private final String apiKey;
    private final String apiSecret;
}
