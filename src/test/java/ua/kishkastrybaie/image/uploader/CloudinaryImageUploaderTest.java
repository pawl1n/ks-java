package ua.kishkastrybaie.image.uploader;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CloudinaryImageUploaderTest {
  @Mock Uploader uploader;
  @Mock Cloudinary cloudinary;
  @InjectMocks CloudinaryImageUploader imageUploader;

  @Test
  void shouldUpload() throws IOException {
    // given
    String base64encodedImage = "base64encodedImage";
    String filename = "filename";

    Map<?, ?> expectedResponse =
        Map.of("eager", List.of(Map.of("format", "webp", "url", "https://google.com/")));

    given(cloudinary.uploader()).willReturn(uploader);
    given(uploader.upload(any(byte[].class), anyMap())).willReturn(expectedResponse);

    // when
    URL url = imageUploader.upload(base64encodedImage, filename);

    // then
    then(url).isEqualTo(new URL("https://google.com/"));
  }

  @Test
  void shouldSaveInDefaultFormatWhenTransformationFailed() throws IOException {
    // given
    String base64encodedImage = "base64encodedImage";
    String filename = "filename";

    Map<?, ?> expectedResponse = Map.of("eager", List.of(), "url", "https://google.com/");

    given(cloudinary.uploader()).willReturn(uploader);
    given(uploader.upload(any(byte[].class), anyMap())).willReturn(expectedResponse);

    // when
    URL url = imageUploader.upload(base64encodedImage, filename);

    // then
    then(url).isEqualTo(new URL("https://google.com/"));
  }

  @Test
  void shouldSaveInDefaultFormatWhenEagerListContainsString() throws IOException {
    // given
    String base64encodedImage = "base64encodedImage";
    String filename = "filename";

    Map<?, ?> expectedResponse = Map.of("eager", List.of("Not Map"), "url", "https://google.com/");

    given(cloudinary.uploader()).willReturn(uploader);
    given(uploader.upload(any(byte[].class), anyMap())).willReturn(expectedResponse);

    // when
    URL url = imageUploader.upload(base64encodedImage, filename);

    // then
    then(url).isEqualTo(new URL("https://google.com/"));
  }

  @Test
  void shouldSaveInDefaultFormatWhenEagerNotList() throws IOException {
    // given
    String base64encodedImage = "base64encodedImage";
    String filename = "filename";

    Map<?, ?> expectedResponse =
            Map.of("eager", List.of(Map.of("format", Map.of())), "url", "https://google.com/");

    given(cloudinary.uploader()).willReturn(uploader);
    given(uploader.upload(any(byte[].class), anyMap())).willReturn(expectedResponse);

    // when
    URL url = imageUploader.upload(base64encodedImage, filename);

    // then
    then(url).isEqualTo(new URL("https://google.com/"));
  }

  @Test
  void shouldSaveInDefaultFormatWhenIncorrectFormatInResponse() throws IOException {
    // given
    String base64encodedImage = "base64encodedImage";
    String filename = "filename";

    Map<?, ?> expectedResponse =
            Map.of("eager", List.of(Map.of("format", "not webp")), "url", "https://google.com/");

    given(cloudinary.uploader()).willReturn(uploader);
    given(uploader.upload(any(byte[].class), anyMap())).willReturn(expectedResponse);

    // when
    URL url = imageUploader.upload(base64encodedImage, filename);

    // then
    then(url).isEqualTo(new URL("https://google.com/"));
  }

  @Test
  void shouldSaveInDefaultFormatWhenFormatIsNull() throws IOException {
    // given
    String base64encodedImage = "base64encodedImage";
    String filename = "filename";

    Map<?, ?> expectedResponse = Map.of("eager", "Not List", "url", "https://google.com/");

    given(cloudinary.uploader()).willReturn(uploader);
    given(uploader.upload(any(byte[].class), anyMap())).willReturn(expectedResponse);

    // when
    URL url = imageUploader.upload(base64encodedImage, filename);

    // then
    then(url).isEqualTo(new URL("https://google.com/"));
  }

  @Test
  void shouldThrowExceptionWhenEmptyImage() {
    // given
    String filename = "filename";

    // when
    String base64encodedImage = "";

    // then
    thenThrownBy(() -> imageUploader.upload(base64encodedImage, filename))
        .isInstanceOf(ImageUploadException.class)
        .hasMessageEndingWith("Base64 encoded image is empty");
  }

  @Test
  void shouldThrowExceptionWhenEmptyFilename() {
    // given
    String base64encodedImage = "base64encodedImage";

    // when
    String filename = "";

    // then
    thenThrownBy(() -> imageUploader.upload(base64encodedImage, filename))
        .isInstanceOf(ImageUploadException.class)
        .hasMessageEndingWith("Filename is empty");
  }

  @Test
  void shouldThrowExceptionWhenUploadFailed() throws IOException {
    // given
    String base64encodedImage = "base64encodedImage";
    String filename = "filename";

    given(cloudinary.uploader()).willReturn(uploader);

    // when
    given(uploader.upload(any(byte[].class), any(Map.class))).willThrow(new IOException());

    // then
    thenThrownBy(() -> imageUploader.upload(base64encodedImage, filename))
        .isInstanceOf(ImageUploadException.class)
        .hasMessageStartingWith("Error while uploading image: ");
  }
}
