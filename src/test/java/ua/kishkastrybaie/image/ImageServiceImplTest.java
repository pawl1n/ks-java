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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import ua.kishkastrybaie.image.uploader.ImageUploader;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {
  private static Image image;
  private static ImageDto imageDto;
  private static ImageRequestDto imageRequestDto;
  @Mock private ImageRepository imageRepository;
  @Mock private ImageModelAssembler imageModelAssembler;
  @Mock private ImageUploader imageUploader;
  @Mock private PagedResourcesAssembler<Image> pagedResourcesAssembler;
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
    Page<Image> images = new PageImpl<>(List.of(image));
    PagedModel<ImageDto> imageDtoCollectionModel =
        PagedModel.of(List.of(imageDto), new PagedModel.PageMetadata(5, 0, 1));

    given(imageRepository.findAll(PageRequest.ofSize(5))).willReturn(images);
    given(pagedResourcesAssembler.toModel(images, imageModelAssembler))
        .willReturn(imageDtoCollectionModel);

    // when
    CollectionModel<ImageDto> actualResult = imageService.findAll(PageRequest.ofSize(5));

    // then
    then(actualResult).isEqualTo(imageDtoCollectionModel);
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
  }

  @Test
  void shouldNotFindByIdWhenInvalidId() {
    // given

    // when
    when(imageRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> imageService.findById(1L)).isInstanceOf(ImageNotFoundException.class);
    verifyNoInteractions(imageModelAssembler);
  }

  @Test
  void shouldCreate() throws MalformedURLException {
    // given
    Image newImage = new Image();
    newImage.setName("image");
    newImage.setDescription("description");
    newImage.setUrl(new URL("https://pbs.twimg.com/media/E4bu1cRXoAMRnXz.jpg"));

    given(imageUploader.upload(imageRequestDto.base64Image(), imageRequestDto.name()))
        .willReturn(image.getUrl());
    given(
            imageRepository.save(
                argThat(
                    i ->
                        i.getId() == null
                            && i.getDescription().equals(newImage.getDescription())
                            && i.getUrl().toString().equals(newImage.getUrl().toString())
                            && i.getName().equals(newImage.getName()))))
        .willReturn(image);
    given(imageModelAssembler.toModel(image)).willReturn(imageDto);

    // when
    ImageDto actualCreateResult = imageService.create(imageRequestDto);

    // then
    then(actualCreateResult).isEqualTo(imageDto);
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
    given(
            imageRepository.save(
                argThat(
                    i ->
                        i.getId().equals(1L)
                            && i.getDescription().equals(newImage.getDescription())
                            && i.getUrl().toString().equals(newImage.getUrl().toString())
                            && i.getName().equals(newImage.getName()))))
        .willReturn(newImage);
    given(imageModelAssembler.toModel(newImage)).willReturn(newImageDto);

    // when
    ImageDto actualCreateResult = imageService.replace(1L, newImageRequestDto);

    // then
    then(actualCreateResult).isEqualTo(newImageDto);
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
    verify(imageRepository).delete(image);
  }

  @Test
  void shouldNotDeleteByIdWhenInvalidId() {
    // given

    // when
    when(imageRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> imageService.deleteById(1L)).isInstanceOf(ImageNotFoundException.class);
  }
}
