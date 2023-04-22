package ua.kishkastrybaie.product;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.net.MalformedURLException;
import java.net.URL;
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
import ua.kishkastrybaie.category.*;
import ua.kishkastrybaie.image.Image;
import ua.kishkastrybaie.image.ImageRepository;
import ua.kishkastrybaie.shared.AuthorizationService;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
  private static Category category;
  private static Product product1;
  private static Product product2;
  private static ProductDto productDto1;
  private static ProductDto productDto2;
  private static ProductRequestDto productRequestDto;
  private static Image image;
  @Mock private ProductRepository productRepository;
  @Mock private ProductModelAssembler productModelAssembler;
  @Mock private CategoryModelAssembler categoryModelAssembler;
  @Mock private CategoryRepository categoryRepository;
  @Mock private ImageRepository imageRepository;
  @Mock private PagedResourcesAssembler<Product> pagedResourcesAssembler;
  @Mock private AuthorizationService authorizationService;
  @InjectMocks private ProductServiceImpl productService;

  @BeforeEach
  void setUp() throws MalformedURLException {
    category = new Category();
    category.setId(1L);
    category.setName("Category 1");

    image = new Image();
    image.setName("image");
    image.setUrl(new URL("https://pbs.twimg.com/media/E4bu1cRXoAMRnXz.jpg"));

    product1 = new Product();
    product1.setId(1L);
    product1.setName("Product 1");
    product1.setDescription("Description 1");
    product1.setCategory(category);
    product1.setMainImage(image);

    product2 = new Product();
    product2.setId(2L);
    product2.setName("Product 2");

    productDto1 =
        new ProductDto(
            1L,
            "Product 1",
            "Description 1",
            new CategoryDto(1L, "Category 1", null),
            new URL("https://pbs.twimg.com/media/E4bu1cRXoAMRnXz.jpg"));

    productDto2 = new ProductDto(2L, "Product 2", null, null, null);

    productRequestDto =
        new ProductRequestDto(
            "Product 1",
            "Description 1",
            1L,
            new URL("https://pbs.twimg.com/media/E4bu1cRXoAMRnXz.jpg"));
  }

  @Test
  void shouldFindAll() {
    // given
    Page<Product> products = new PageImpl<>(List.of(product1, product2));
    PagedModel<ProductDto> productDtoCollectionModel =
        PagedModel.of(List.of(productDto1, productDto2), new PagedModel.PageMetadata(5, 0, 2));

    given(authorizationService.isAdmin()).willReturn(true);
    given(productRepository.findAll(PageRequest.ofSize(5))).willReturn(products);
    given(pagedResourcesAssembler.toModel(products, productModelAssembler))
        .willReturn(productDtoCollectionModel);

    // when
    CollectionModel<ProductDto> response = productService.findAll(PageRequest.ofSize(5));

    // then
    then(response).hasSize(2).usingRecursiveComparison().isEqualTo(productDtoCollectionModel);
  }

  @Test
  void shouldFindAllWhenIsNotAdmin() {
    // given
    Page<Product> products = new PageImpl<>(List.of(product1, product2));
    PagedModel<ProductDto> productDtoCollectionModel =
        PagedModel.of(List.of(productDto1, productDto2), new PagedModel.PageMetadata(5, 0, 2));

    given(authorizationService.isAdmin()).willReturn(false);
    given(productRepository.findAllByProductItemsIsNotNull(PageRequest.ofSize(5)))
        .willReturn(products);
    given(pagedResourcesAssembler.toModel(products, productModelAssembler))
        .willReturn(productDtoCollectionModel);

    // when
    CollectionModel<ProductDto> response = productService.findAll(PageRequest.ofSize(5));

    // then
    then(response).hasSize(2).usingRecursiveComparison().isEqualTo(productDtoCollectionModel);
  }

  @Test
  void shouldFindById() {
    // given
    given(productRepository.findById(1L)).willReturn(Optional.of(product1));
    given(productModelAssembler.toModel(product1)).willReturn(productDto1);

    // when
    ProductDto response = productService.findById(1L);

    // then
    then(response).usingRecursiveComparison().isEqualTo(productDto1);
  }

  @Test
  void shouldNotFindByIdWhenInvalidId() {
    // given

    // when
    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> productService.findById(1L)).isInstanceOf(ProductNotFoundException.class);
    verifyNoInteractions(productModelAssembler);
  }

  @Test
  void shouldCreate() {
    // given
    given(categoryRepository.existsById(1L)).willReturn(true);
    given(categoryRepository.getReferenceById(1L)).willReturn(category);
    given(imageRepository.existsByUrl(productRequestDto.mainImage())).willReturn(true);
    given(imageRepository.getReferenceByUrl(productRequestDto.mainImage())).willReturn(image);
    given(
            productRepository.save(
                argThat(
                    product ->
                        product.getName().equals(productRequestDto.name())
                            && product.getDescription().equals(productRequestDto.description()))))
        .willReturn(product1);
    given(productModelAssembler.toModel(product1)).willReturn(productDto1);

    // when
    ProductDto response = productService.create(productRequestDto);

    // then
    then(response).usingRecursiveComparison().isEqualTo(productDto1);
  }

  @Test
  void shouldReplace() throws MalformedURLException {
    // given
    Product changedProduct = new Product();
    changedProduct.setId(2L);
    changedProduct.setName("Product 1");
    changedProduct.setDescription("Description 1");
    changedProduct.setCategory(category);
    changedProduct.setMainImage(image);

    ProductDto changedProductDto =
        new ProductDto(
            2L,
            "Product 1",
            "Description 1",
            new CategoryDto(1L, "Category 1", null),
            new URL("https://pbs.twimg.com/media/E4bu1cRXoAMRnXz.jpg"));

    given(categoryRepository.existsById(1L)).willReturn(true);
    given(categoryRepository.getReferenceById(1L)).willReturn(category);
    given(imageRepository.existsByUrl(productRequestDto.mainImage())).willReturn(true);
    given(imageRepository.getReferenceByUrl(productRequestDto.mainImage())).willReturn(image);
    given(productRepository.findById(2L)).willReturn(Optional.of(product2));
    given(
            productRepository.save(
                argThat(
                    product ->
                        product.getId().equals(2L)
                            && product.getName().equals(productRequestDto.name())
                            && product.getDescription().equals(productRequestDto.description()))))
        .willReturn(changedProduct);
    given(productModelAssembler.toModel(changedProduct)).willReturn(changedProductDto);

    // when
    ProductDto response = productService.replace(2L, productRequestDto);

    // then
    then(response).usingRecursiveComparison().isEqualTo(changedProductDto);
  }

  @Test
  void shouldNotReplaceWhenInvalidId() {
    // given
    given(categoryRepository.existsById(1L)).willReturn(true);
    given(categoryRepository.getReferenceById(1L)).willReturn(category);
    given(imageRepository.existsByUrl(productRequestDto.mainImage())).willReturn(true);
    given(imageRepository.getReferenceByUrl(productRequestDto.mainImage())).willReturn(image);

    // when
    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> productService.replace(1L, productRequestDto))
        .isInstanceOf(ProductNotFoundException.class);
    verifyNoInteractions(productModelAssembler);
  }

  @Test
  void shouldDeleteById() {
    // given
    given(productRepository.findById(1L)).willReturn(Optional.of(product1));

    // when
    productService.deleteById(1L);

    // then
    verify(productRepository).delete(product1);
  }

  @Test
  void shouldNotDeleteByIdWhenInvalidId() {
    // given

    // when
    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> productService.deleteById(1L)).isInstanceOf(ProductNotFoundException.class);
    verifyNoInteractions(productModelAssembler);
  }

  @Test
  void shouldGetProductCategory() {
    // given
    CategoryDto categoryDto = new CategoryDto(1L, "Category 1", null);
    given(productRepository.findById(1L)).willReturn(Optional.of(product1));
    given(categoryModelAssembler.toModel(category)).willReturn(categoryDto);

    // when
    CategoryDto response = productService.getProductCategory(1L);

    // then
    then(response).usingRecursiveComparison().isEqualTo(categoryDto);
  }

  @Test
  void shouldNotGetProductCategoryWhenInvalidId() {
    // given

    // when
    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> productService.getProductCategory(1L))
        .isInstanceOf(ProductNotFoundException.class);
    verify(productRepository).findById(1L);
    verifyNoInteractions(categoryModelAssembler);
  }

  @Test
  void shouldNotGetProductCategoryWhenNoCategory() {
    // given

    // when
    when(productRepository.findById(2L)).thenReturn(Optional.of(product2));

    // then
    thenThrownBy(() -> productService.getProductCategory(2L))
        .isInstanceOf(CategoryNotFoundException.class);
    verifyNoInteractions(categoryModelAssembler);
  }
}
