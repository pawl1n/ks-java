package ua.kishkastrybaie.image;

import java.net.URL;

@FunctionalInterface
public interface ImageUploader {
  URL upload(String base64encodedImage, String name);
}
