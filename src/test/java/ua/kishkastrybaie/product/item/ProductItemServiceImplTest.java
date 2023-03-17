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
import ua.kishkastrybaie.variation.option.VariationOption;
import ua.kishkastrybaie.variation.option.VariationOptionDto;
import ua.kishkastrybaie.variation.option.VariationOptionId;
import ua.kishkastrybaie.variation.option.VariationOptionRepository;

@ExtendWith(MockitoExtension.class)
class ProductItemServiceImplTest {
  Product product;
  ProductItem productItem1;
  ProductItem productItem2;
  ProductItemDto productItemDto1;
  ProductItemDto productItemDto2;
  ProductItemRequestDto productItemRequestDto1;
  Variation variation;

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

    VariationOption variationOption = new VariationOption();
    variationOption.setVariation(variation);
    variationOption.setValue("value");

    VariationOptionDto variationOptionDto = new VariationOptionDto(1L, "value");
    ProductItemRequestDto.VariationOptionRequestDto variationOptionRequestDto =
        new ProductItemRequestDto.VariationOptionRequestDto("value", 1L);

    Category category = new Category();
    category.setChildren(new HashSet<>());
    category.setId(1L);
    category.setName("Name");
    category.setParentCategory(new Category());

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

    productItem1 = new ProductItem();
    productItem1.setId(1L);
    productItem1.setPrice(10.0d);
    productItem1.setProduct(product);
    productItem1.setSku("sample");
    productItem1.setStock(1);
    productItem1.setVariationOptions(new HashSet<>(List.of(variationOption)));

    productItem2 = new ProductItem();
    productItem2.setId(2L);
    productItem2.setPrice(1000.0d);
    productItem2.setProduct(product);
    productItem2.setSku("1111");
    productItem2.setStock(2);
    productItem2.setVariationOptions(new HashSet<>());

    productItemDto1 = new ProductItemDto();
    productItemDto1.setId(1L);
    productItemDto1.setPrice(10.0d);
    productItemDto1.setSku("sample");
    productItemDto1.setStock(1);
    productItemDto1.setVariationOptions(new HashSet<>(List.of(variationOptionDto)));

    productItemDto2 = new ProductItemDto();
    productItemDto2.setId(2L);
    productItemDto2.setPrice(1000.0d);
    productItemDto2.setSku("1111");
    productItemDto2.setStock(2);
    productItemDto2.setVariationOptions(new HashSet<>());

    productItemRequestDto1 =
        new ProductItemRequestDto(
            10.0d, 1, "sample", new HashSet<>(List.of(variationOptionRequestDto)));
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
    verify(productRepository).existsById(1L);
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
    verify(productRepository).existsById(1L);
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
    verify(productRepository).existsById(1L);
    verify(productItemRepository).findByIdAndProductId(1L, 1L);
    verifyNoInteractions(representationModelAssembler);
  }

  @Test
  void shouldCreate() {
    // given
    given(productRepository.existsById(1L)).willReturn(true);
    given(variationRepository.existsById(1L)).willReturn(true);
    given(variationRepository.getReferenceById(1L)).willReturn(variation);
    given(variationOptionRepository.existsById(new VariationOptionId(variation, "value"))).willReturn(true);
    given(productRepository.getReferenceById(1L)).willReturn(product);
    given(
            productItemRepository.save(
                argThat(
                    productItem ->
                        productItem.getPrice().equals(productItem1.getPrice())
                            && productItem.getProduct().equals(productItem1.getProduct())
                            && productItem.getSku().equals(productItem1.getSku())
                            && productItem.getStock().equals(productItem1.getStock())
                            && productItem
                                .getVariationOptions().size() == 1)))
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
    verify(productRepository).existsById(1L);
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
    thenThrownBy(() -> productItemServiceImpl.create(1L, productItemRequestDto1)).isInstanceOf(VariationNotFoundException.class);
    verify(productRepository).existsById(1L);
    verify(variationRepository).existsById(1L);
    verifyNoMoreInteractions(variationRepository);
    verifyNoInteractions(variationOptionRepository);
    verifyNoInteractions(productItemRepository);
    verifyNoInteractions(representationModelAssembler);
  }
}
