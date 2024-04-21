package ua.kishkastrybaie.product.item;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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
import ua.kishkastrybaie.category.Category;
import ua.kishkastrybaie.image.Image;
import ua.kishkastrybaie.product.Product;
import ua.kishkastrybaie.product.ProductNotFoundException;
import ua.kishkastrybaie.product.ProductRepository;
import ua.kishkastrybaie.variation.Variation;
import ua.kishkastrybaie.variation.VariationNotFoundException;
import ua.kishkastrybaie.variation.VariationRepository;
import ua.kishkastrybaie.variation.option.*;

@ExtendWith(MockitoExtension.class)
class ProductItemServiceImplTest {
  Product product;
  ProductItem productItem1;
  ProductItem productItem2;
  ProductItemDto productItemDto1;
  ProductItemDto productItemDto2;
  ProductItemRequestDto productItemRequestDto1;
  Variation variation;
  VariationOption variationOption;

  @Mock private ProductItemModelAssembler representationModelAssembler;
  @Mock private ProductItemRepository productItemRepository;
  @Mock private VariationOptionRepository variationOptionRepository;
  @Mock private VariationRepository variationRepository;
  @Mock private ProductRepository productRepository;
  @Mock private PagedResourcesAssembler<ProductItem> pagedResourcesAssembler;
  @InjectMocks private ProductItemServiceImpl productItemServiceImpl;

  @BeforeEach
  void setUp() throws MalformedURLException {
    variation = new Variation();
    variation.setId(1L);
    variation.setName("name");

    variationOption = new VariationOption();
    variationOption.setVariation(variation);
    variationOption.setValue("value");

    VariationOptionDto variationOptionDto = new VariationOptionDto(1L, "value");
    ProductItemRequestDto.VariationOptionRequestDto variationOptionRequestDto =
        new ProductItemRequestDto.VariationOptionRequestDto("value", 1L);

    Category category = new Category();
    category.setId(1L);
    category.setName("Name");

    Image image = new Image();
    image.setDescription("The characteristics of someone or something");
    image.setId(1L);
    image.setName("Name");
    image.setUrl(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL());

    product = new Product();
    product.setCategory(category);
    product.setDescription("The characteristics of someone or something");
    product.setId(1L);
    product.setMainImage(image);
    product.setName("Name");
    product.setPrice(10.0d);
    product.setSku("sample");

    productItem1 = new ProductItem();
    productItem1.setId(1L);
    productItem1.setProduct(product);
    productItem1.setStock(1);
    productItem1.setVariationOptions(new HashSet<>(List.of(variationOption)));

    productItem2 = new ProductItem();
    productItem2.setId(2L);
    productItem2.setProduct(product);
    productItem2.setStock(2);
    productItem2.setVariationOptions(new HashSet<>());

    productItemDto1 =
        new ProductItemDto(1L, "Name", "value", 1, new HashSet<>(List.of(variationOptionDto)));
    productItemDto2 = new ProductItemDto(2L, "Name", "value", 2, new HashSet<>());

    productItemRequestDto1 =
        new ProductItemRequestDto(1, new HashSet<>(List.of(variationOptionRequestDto)));
  }

  @Test
  void shouldFindAllByProductId() {
    // given
    Page<ProductItem> productItems = new PageImpl<>(List.of(productItem1, productItem2));
    PagedModel<ProductItemDto> productItemDtoCollectionModel =
        PagedModel.of(
            List.of(productItemDto1, productItemDto2), new PagedModel.PageMetadata(5, 0, 2));

    given(productRepository.existsById(1L)).willReturn(true);
    given(productItemRepository.findAllByProductId(1L, PageRequest.ofSize(5)))
        .willReturn(productItems);
    given(pagedResourcesAssembler.toModel(productItems, representationModelAssembler))
        .willReturn(productItemDtoCollectionModel);

    // when
    CollectionModel<ProductItemDto> actualResult =
        productItemServiceImpl.findAllByProductId(1L, PageRequest.ofSize(5));

    // then
    then(actualResult).isEqualTo(productItemDtoCollectionModel);
  }

  @Test
  void shouldNotFindAllByProductIdWhenInvalidId() {
    // given

    // when
    when(productRepository.existsById(1L)).thenReturn(false);

    // then
    thenThrownBy(() -> productItemServiceImpl.findAllByProductId(1L, PageRequest.ofSize(5)))
        .isInstanceOf(ProductNotFoundException.class);
    verifyNoInteractions(productItemRepository);
    verifyNoInteractions(pagedResourcesAssembler);
  }

  @Test
  void shouldFindByProductIdAndId() {
    // given
    given(productRepository.existsById(1L)).willReturn(true);
    given(productItemRepository.findByIdAndProductId(1L, 1L)).willReturn(Optional.of(productItem1));
    given(representationModelAssembler.toModel(productItem1)).willReturn(productItemDto1);

    // when
    ProductItemDto actualResult = productItemServiceImpl.findByProductIdAndId(1L, 1L);

    // then
    then(actualResult).isEqualTo(productItemDto1);
  }

  @Test
  void shouldNotFindByProductIdAndIdWhenInvalidProductId() {
    // given

    // when
    when(productRepository.existsById(1L)).thenReturn(false);

    // then
    thenThrownBy(() -> productItemServiceImpl.findByProductIdAndId(1L, 1L))
        .isInstanceOf(ProductNotFoundException.class);
    verifyNoInteractions(productItemRepository);
    verifyNoInteractions(representationModelAssembler);
  }

  @Test
  void shouldNotFindByProductIdAndIdWhenInvalidId() {
    // given
    given(productRepository.existsById(1L)).willReturn(true);

    // when
    when(productItemRepository.findByIdAndProductId(1L, 1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> productItemServiceImpl.findByProductIdAndId(1L, 1L))
        .isInstanceOf(ProductItemNotFoundException.class);
    verifyNoInteractions(representationModelAssembler);
  }

  @Test
  void shouldCreate() {
    // given
    given(productRepository.existsById(1L)).willReturn(true);
    given(variationRepository.existsById(1L)).willReturn(true);
    given(variationRepository.getReferenceById(1L)).willReturn(variation);
    given(variationOptionRepository.existsById(new VariationOptionId(variation, "value")))
        .willReturn(true);
    given(productRepository.getReferenceById(1L)).willReturn(product);
    given(
            productItemRepository.save(
                argThat(
                    productItem ->
                        productItem.getProduct().equals(productItem1.getProduct())
                            && productItem.getStock().equals(productItem1.getStock())
                            && productItem.getVariationOptions().size() == 1)))
        .willReturn(productItem1);
    given(representationModelAssembler.toModel(productItem1)).willReturn(productItemDto1);

    // when
    ProductItemDto actualResult = productItemServiceImpl.create(1L, productItemRequestDto1);

    // then
    then(actualResult).isEqualTo(productItemDto1);
  }

  @Test
  void shouldNotCreateWhenInvalidProductId() {
    // given

    // when
    when(productRepository.existsById(1L)).thenReturn(false);

    // then
    thenThrownBy(() -> productItemServiceImpl.create(1L, productItemRequestDto1))
        .isInstanceOf(ProductNotFoundException.class);
    verifyNoInteractions(productItemRepository);
    verifyNoInteractions(representationModelAssembler);
  }

  @Test
  void shouldNotCreateWhenInvalidVariationId() {
    // given
    given(productRepository.existsById(1L)).willReturn(true);

    // when
    when(variationRepository.existsById(1L)).thenReturn(false);

    // then
    thenThrownBy(() -> productItemServiceImpl.create(1L, productItemRequestDto1))
        .isInstanceOf(VariationNotFoundException.class);
    verifyNoMoreInteractions(variationRepository);
    verifyNoInteractions(variationOptionRepository);
    verifyNoInteractions(productItemRepository);
    verifyNoInteractions(representationModelAssembler);
  }

  @Test
  void shouldNotCreateWhenInvalidVariationOptionId() {
    // given
    given(productRepository.existsById(1L)).willReturn(true);
    given(variationRepository.existsById(1L)).willReturn(true);
    given(variationRepository.getReferenceById(1L)).willReturn(variation);

    // when
    when(variationOptionRepository.existsById(new VariationOptionId(variation, "value")))
        .thenReturn(false);

    // then
    thenThrownBy(() -> productItemServiceImpl.create(1L, productItemRequestDto1))
        .isInstanceOf(VariationOptionNotFoundException.class);
    verifyNoMoreInteractions(variationOptionRepository);
    verifyNoInteractions(productItemRepository);
    verifyNoInteractions(representationModelAssembler);
  }

  @Test
  void shouldReplace() {
    // given
    given(productRepository.existsById(1L)).willReturn(true);
    given(variationRepository.existsById(1L)).willReturn(true);
    given(variationRepository.getReferenceById(1L)).willReturn(variation);
    given(variationOptionRepository.existsById(new VariationOptionId(variation, "value")))
        .willReturn(true);
    given(productItemRepository.findByIdAndProductId(2L, 1L)).willReturn(Optional.of(productItem2));
    given(
            productItemRepository.save(
                argThat(
                    productItem ->
                        productItem.getId().equals(2L)
                            && productItem.getProduct().equals(productItem1.getProduct())
                            && productItem.getStock().equals(productItem1.getStock())
                            && productItem.getVariationOptions().size() == 1)))
        .willReturn(productItem1);
    given(representationModelAssembler.toModel(productItem1)).willReturn(productItemDto1);

    // when
    ProductItemDto actualResult = productItemServiceImpl.replace(1L, 2L, productItemRequestDto1);

    // then
    then(actualResult).isEqualTo(productItemDto1);
  }

  @Test
  void shouldNotReplaceWhenInvalidProductId() {
    // given

    // when
    when(productRepository.existsById(1L)).thenReturn(false);

    // then
    thenThrownBy(() -> productItemServiceImpl.replace(1L, 1L, productItemRequestDto1))
        .isInstanceOf(ProductNotFoundException.class);
    verify(productRepository).existsById(1L);
    verifyNoInteractions(productItemRepository);
    verifyNoInteractions(representationModelAssembler);
  }

  @Test
  void shouldNotReplaceWhenInvalidVariationId() {
    // given
    given(productRepository.existsById(1L)).willReturn(true);

    // when
    when(variationRepository.existsById(1L)).thenReturn(false);

    // then
    thenThrownBy(() -> productItemServiceImpl.replace(1L, 1L, productItemRequestDto1))
        .isInstanceOf(VariationNotFoundException.class);
    verifyNoMoreInteractions(variationRepository);
    verifyNoInteractions(variationOptionRepository);
    verifyNoInteractions(productItemRepository);
    verifyNoInteractions(representationModelAssembler);
  }

  @Test
  void shouldNotReplaceWhenInvalidVariationOptionId() {
    // given
    given(productRepository.existsById(1L)).willReturn(true);
    given(variationRepository.existsById(1L)).willReturn(true);
    given(variationRepository.getReferenceById(1L)).willReturn(variation);

    // when
    when(variationOptionRepository.existsById(new VariationOptionId(variation, "value")))
        .thenReturn(false);

    // then
    thenThrownBy(() -> productItemServiceImpl.replace(1L, 1L, productItemRequestDto1))
        .isInstanceOf(VariationOptionNotFoundException.class);
    verifyNoMoreInteractions(variationOptionRepository);
    verifyNoInteractions(productItemRepository);
    verifyNoInteractions(representationModelAssembler);
  }

  @Test
  void shouldNotReplaceWhenInvalidProductItemId() {
    // given
    given(productRepository.existsById(1L)).willReturn(true);
    given(variationRepository.existsById(1L)).willReturn(true);
    given(variationRepository.getReferenceById(1L)).willReturn(variation);
    given(variationOptionRepository.existsById(new VariationOptionId(variation, "value")))
        .willReturn(true);
    given(variationOptionRepository.getReferenceById(new VariationOptionId(variation, "value")))
        .willReturn(variationOption);

    // when
    when(productItemRepository.findByIdAndProductId(1L, 1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> productItemServiceImpl.replace(1L, 1L, productItemRequestDto1))
        .isInstanceOf(ProductItemNotFoundException.class);
    verifyNoMoreInteractions(productItemRepository);
    verifyNoInteractions(representationModelAssembler);
  }

  @Test
  void shouldDelete() {
    // given
    given(productRepository.existsById(1L)).willReturn(true);
    given(productItemRepository.findByIdAndProductId(1L, 1L)).willReturn(Optional.of(productItem1));

    // when
    productItemServiceImpl.delete(1L, 1L);

    // then
    verify(productItemRepository).delete(productItem1);
    verifyNoMoreInteractions(productRepository);
    verifyNoMoreInteractions(productItemRepository);
    verifyNoInteractions(variationRepository);
    verifyNoInteractions(variationOptionRepository);
    verifyNoInteractions(representationModelAssembler);
  }

  @Test
  void shouldNotDeleteWhenInvalidProductId() {
    // given

    // when
    when(productRepository.existsById(1L)).thenReturn(false);

    // then
    thenThrownBy(() -> productItemServiceImpl.delete(1L, 1L))
        .isInstanceOf(ProductNotFoundException.class);
    verifyNoMoreInteractions(productRepository);
    verifyNoInteractions(productItemRepository);
    verifyNoInteractions(variationRepository);
    verifyNoInteractions(variationOptionRepository);
    verifyNoInteractions(representationModelAssembler);
  }

  @Test
  void shouldNotDeleteWhenInvalidProductItemId() {
    // given
    given(productRepository.existsById(1L)).willReturn(true);

    // when
    when(productItemRepository.findByIdAndProductId(1L, 1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> productItemServiceImpl.delete(1L, 1L))
        .isInstanceOf(ProductNotFoundException.class);
    verifyNoMoreInteractions(productRepository);
    verifyNoMoreInteractions(productItemRepository);
    verifyNoInteractions(variationRepository);
    verifyNoInteractions(variationOptionRepository);
    verifyNoInteractions(representationModelAssembler);
  }
}
