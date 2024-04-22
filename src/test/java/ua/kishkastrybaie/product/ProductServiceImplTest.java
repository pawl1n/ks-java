package ua.kishkastrybaie.product;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import ua.kishkastrybaie.category.*;
import ua.kishkastrybaie.image.Image;
import ua.kishkastrybaie.image.ImageRepository;
import ua.kishkastrybaie.product.details.ProductDetailsDto;
import ua.kishkastrybaie.product.details.ProductDetailsModelAssembler;
import ua.kishkastrybaie.shared.BreadCrumb;

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
  @Mock private ProductDetailsModelAssembler productDetailsModelAssembler;
  @Mock private CategoryModelAssembler categoryModelAssembler;
  @Mock private CategoryRepository categoryRepository;
  @Mock private ImageRepository imageRepository;
  @Mock private PagedResourcesAssembler<Product> pagedResourcesAssembler;
  @InjectMocks private ProductServiceImpl productService;

  @BeforeEach
  void setUp() throws URISyntaxException, MalformedURLException {
    category = new Category();
    category.setId(1L);
    category.setName("Category 1");

    image = new Image();
    image.setName("image");
    image.setUrl(new URI("https://pbs.twimg.com/media/E4bu1cRXoAMRnXz.jpg").toURL());

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
            "product-1",
            null,
            1000.0,
            new CategoryDto(1L, "Category 1", null, null, null),
            new URI("https://pbs.twimg.com/media/E4bu1cRXoAMRnXz.jpg").toURL());

    productDto2 = new ProductDto(2L, "Product 2", null, null, null, 1000.0, null, null);

    productRequestDto =
        new ProductRequestDto(
            "Product 1",
            "Description 1",
            1L,
            new URI("https://pbs.twimg.com/media/E4bu1cRXoAMRnXz.jpg").toURL(),
            null,
            null,
            1000.0);
  }

  @Test
  void shouldFindAll() {
    // given
    Page<Product> products = new PageImpl<>(List.of(product1, product2));
    PagedModel<ProductDto> productDtoCollectionModel =
        PagedModel.of(List.of(productDto1, productDto2), new PagedModel.PageMetadata(5, 0, 2));

    given(productRepository.findAll(PageRequest.ofSize(5))).willReturn(products);
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
  void shouldFindBySlug() {
    // given
    given(productRepository.findBySlug("product_1")).willReturn(Optional.of(product1));
    given(productModelAssembler.toModel(product1)).willReturn(productDto1);

    // when
    ProductDto response = productService.findBySlug("product_1");

    // then
    then(response).isEqualTo(productDto1);
  }

  @Test
  void shouldNotFindBySlugWhenInvalidSlug() {
    // given

    // when
    when(productRepository.findBySlug("product_1")).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> productService.findBySlug("product_1"))
        .isInstanceOf(ProductNotFoundException.class);
  }

  @Test
  void shouldGetDetailsBySlug() {
    // given
    BreadCrumb breadcrumb = new BreadCrumb(category.getName(), category.getPath(), null);
    ProductDetailsDto productDetailsDto = new ProductDetailsDto(productDto1, Set.of(), breadcrumb);

    given(productRepository.findBySlug("product_1")).willReturn(Optional.of(product1));
    given(productDetailsModelAssembler.toModel(product1)).willReturn(productDetailsDto);

    // when
    ProductDetailsDto response = productService.getDetailsBySlug("product_1");

    // then
    then(response).isEqualTo(productDetailsDto);
  }

  @Test
  void shouldNotGetDetailsBySlugWhenInvalidSlug() {
    // given

    // when
    when(productRepository.findBySlug("product_1")).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> productService.getDetailsBySlug("product_1"))
        .isInstanceOf(ProductNotFoundException.class);
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
                            && product.getDescription().equals(productRequestDto.description())
                            && product.getSlug().equals("product_1"))))
        .willReturn(product1);
    given(productModelAssembler.toModel(product1)).willReturn(productDto1);

    // when
    ProductDto response = productService.create(productRequestDto);

    // then
    then(response).usingRecursiveComparison().isEqualTo(productDto1);
  }

  @Test
  void shouldReplace() throws URISyntaxException, MalformedURLException {
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
            "product-1",
            null,
            1000.0,
            new CategoryDto(1L, "Category 1", null, null, null),
            new URI("https://pbs.twimg.com/media/E4bu1cRXoAMRnXz.jpg").toURL());

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
                            && product.getDescription().equals(productRequestDto.description())
                            && product.getSlug().equals("product_1"))))
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
  void shouldSearch() {
    // given
    Pageable pageable = Pageable.ofSize(5);
    List<Product> products = List.of(product1, product2);
    Page<Product> page = new PageImpl<>(products);
    PagedModel<ProductDto> productDtoCollectionModel =
            PagedModel.of(List.of(productDto1, productDto2), new PagedModel.PageMetadata(5, 0, 2));

    given(productRepository.search("product", pageable)).willReturn(page);
    given(pagedResourcesAssembler.toModel(page, productModelAssembler))
            .willReturn(productDtoCollectionModel);

    // when
    CollectionModel<ProductDto> response = productService.search("product", pageable);

    // then
    then(response).usingRecursiveComparison().isEqualTo(productDtoCollectionModel);
  }

  @Test
  void shouldGetProductCategory() {
    // given
    CategoryDto categoryDto = new CategoryDto(1L, "Category 1", null, null, null);
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
