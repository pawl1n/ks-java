package ua.kishkastrybaie.image;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import ua.kishkastrybaie.image.uploader.ImageUploader;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {
  private static Image image;
  private static ImageDto imageDto;
  private static ImageRequestDto imageRequestDto;
  @Mock private ImageRepository imageRepository;
  @Mock private ImageModelAssembler imageModelAssembler;
  @Mock private ImageUploader imageUploader;
  @InjectMocks private ImageServiceImpl imageService;

  @BeforeAll
  static void setUp() throws MalformedURLException {
    image = new Image();
    image.setId(1L);
    image.setName("image");
    image.setDescription("description");
    image.setUrl(new URL("https://pbs.twimg.com/media/E4bu1cRXoAMRnXz.jpg"));

    imageDto =
        new ImageDto(
            1L,
            "image",
            "Description 1",
            new URL("https://pbs.twimg.com/media/E4bu1cRXoAMRnXz.jpg"));

    imageRequestDto = new ImageRequestDto("image", "description", "test");
  }

  @Test
  void shouldFindAll() {
    // given
    List<Image> images = List.of(image);
    CollectionModel<ImageDto> imageDtoCollectionModel = CollectionModel.of(List.of(imageDto));

    given(imageRepository.findAll()).willReturn(images);
    given(imageModelAssembler.toCollectionModel(images)).willReturn(imageDtoCollectionModel);

    // when
    CollectionModel<ImageDto> actualResult = imageService.findAll();

    // then
    then(actualResult).isEqualTo(imageDtoCollectionModel);
    verify(imageRepository).findAll();
    verify(imageModelAssembler).toCollectionModel(images);
  }

  @Test
  void shouldFindById() {
    // given
    given(imageRepository.findById(1L)).willReturn(Optional.of(image));
    given(imageModelAssembler.toModel(image)).willReturn(imageDto);

    // when
    ImageDto actualFindByIdResult = imageService.findById(1L);

    // then
    then(actualFindByIdResult).isEqualTo(imageDto);
    verify(imageRepository).findById(1L);
    verify(imageModelAssembler).toModel(image);
  }

  @Test
  void shouldNotFindByIdWhenInvalidId() {
    // given

    // when
    when(imageRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> imageService.findById(1L)).isInstanceOf(ImageNotFoundException.class);
    verify(imageRepository).findById(1L);
    verifyNoInteractions(imageModelAssembler);
  }

  @Test
  void shouldCreate()throws MalformedURLException {
    // given
    Image newImage = new Image();
    newImage.setName("image");
    newImage.setDescription("description");
    newImage.setUrl(new URL("https://pbs.twimg.com/media/E4bu1cRXoAMRnXz.jpg"));

    given(imageUploader.upload(imageRequestDto.base64Image(), imageRequestDto.name()))
        .willReturn(image.getUrl());
    given(imageRepository.save(newImage)).willReturn(image);
    given(imageModelAssembler.toModel(image)).willReturn(imageDto);

    // when
    ImageDto actualCreateResult = imageService.create(imageRequestDto);

    // then
    then(actualCreateResult).isEqualTo(imageDto);
    verify(imageUploader).upload(imageRequestDto.base64Image(), imageRequestDto.name());
    verify(imageRepository).save(newImage);
    verify(imageModelAssembler).toModel(image);
  }

  @Test
  void shouldReplace() throws MalformedURLException {
    // given
    ImageRequestDto newImageRequestDto = new ImageRequestDto("new", "new", "test");

    Image newImage = new Image();
    newImage.setId(1L);
    newImage.setName("new");
    newImage.setDescription("new");
    newImage.setUrl(new URL("https://pbs.twimg.com/media/E4bu1cRXoAMRnXz.jpg"));

    ImageDto newImageDto =
        new ImageDto(1L, "new", "new", new URL("https://pbs.twimg.com/media/E4bu1cRXoAMRnXz.jpg"));

    given(imageRepository.findById(1L)).willReturn(Optional.of(image));
    given(imageUploader.upload(newImageRequestDto.base64Image(), newImageRequestDto.name()))
        .willReturn(newImage.getUrl());
    given(imageRepository.save(newImage)).willReturn(newImage);
    given(imageModelAssembler.toModel(newImage)).willReturn(newImageDto);

    // when
    ImageDto actualCreateResult = imageService.replace(1L, newImageRequestDto);

    // then
    then(actualCreateResult).isEqualTo(newImageDto);
    verify(imageRepository).findById(1L);
    verify(imageUploader).upload(newImageRequestDto.base64Image(), newImageRequestDto.name());
    verify(imageRepository).save(newImage);
    verify(imageModelAssembler).toModel(newImage);
  }

  @Test
  void shouldNotReplaceWhenInvalidId() {
    // given
    ImageRequestDto newImageRequestDto = new ImageRequestDto("new", "new", "test");

    // when
    when(imageRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> imageService.replace(1L, newImageRequestDto))
        .isInstanceOf(ImageNotFoundException.class);
    verify(imageRepository).findById(1L);
    verifyNoInteractions(imageUploader);
    verifyNoInteractions(imageModelAssembler);
  }

  @Test
  void shouldDeleteById() {
    // given
    given(imageRepository.findById(1L)).willReturn(Optional.of(image));

    // when
    imageService.deleteById(1L);

    // then
    verify(imageRepository).findById(1L);
    verify(imageRepository).delete(image);
  }

  @Test
  void shouldNotDeleteByIdWhenInvalidId() {
    // given

    // when
    when(imageRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> imageService.deleteById(1L)).isInstanceOf(ImageNotFoundException.class);
    verify(imageRepository).findById(1L);
  }
}
