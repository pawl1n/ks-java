package ua.kishkastrybaie.image;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageDecodeException extends RuntimeException {
    public ImageDecodeException() {
        super("Error while decoding image");
        log.error(getMessage());
    }
}
