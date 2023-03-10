package ua.kishkastrybaie.service.implementation;

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
import org.springframework.hateoas.CollectionModel;
import ua.kishkastrybaie.controller.assembler.CategoryModelAssembler;
import ua.kishkastrybaie.controller.dto.ProductDto;
import ua.kishkastrybaie.controller.dto.ProductRequestDto;
import ua.kishkastrybaie.controller.dto.mapper.ProductMapper;
import ua.kishkastrybaie.exception.CategoryNotFoundException;
import ua.kishkastrybaie.exception.ProductNotFoundException;
import ua.kishkastrybaie.repository.ProductRepository;
import ua.kishkastrybaie.repository.entity.Category;
import ua.kishkastrybaie.repository.entity.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
  private static Category category;
  private static Product product1;
  private static Product product2;
  private static ProductDto productDto1;
  private static ProductDto productDto2;
  private static ProductRequestDto productRequestDto;
  @Mock private ProductRepository productRepository;
  @Mock private ProductModelAssembler productModelAssembler;
  @Mock private CategoryModelAssembler categoryModelAssembler;
  @Mock private ProductMapper productMapper;
  @InjectMocks private ProductServiceImpl productService;

  @BeforeEach
  void setUp() throws MalformedURLException {
    category = new Category();
    category.setId(1L);
    category.setName("Category 1");

    product1 = new Product();
    product1.setId(1L);
    product1.setName("Product 1");
    product1.setDescription("Description 1");
    product1.setCategory(category);
    product1.setMainImage(new URL("https://www.google.com"));

    product2 = new Product();
    product2.setId(2L);
    product2.setName("Product 2");

    productDto1 =
        new ProductDto(
            1L, "Product 1", "Description 1", "Category 1", new URL("https://www.google.com"));

    productDto2 = new ProductDto(2L, "Product 2", null, null, null);

    productRequestDto =
        new ProductRequestDto("Product 1", "Description 1", 1L, new URL("https://www.google.com"));
  }

  @Test
  void shouldFindAll() {
    // given
    List<Product> products = List.of(product1, product2);
    CollectionModel<ProductDto> productDtoCollectionModel =
        CollectionModel.of(List.of(productDto1, productDto2));

    given(productRepository.findAll()).willReturn(products);
    given(productModelAssembler.toCollectionModel(products)).willReturn(productDtoCollectionModel);

    // when
    CollectionModel<ProductDto> response = productService.findAll();

    // then
    then(response).hasSize(2).usingRecursiveComparison().isEqualTo(productDtoCollectionModel);
    verify(productRepository).findAll();
    verify(productModelAssembler).toCollectionModel(products);
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
    verify(productRepository).findById(1L);
    verify(productModelAssembler).toModel(product1);
  }

  @Test
  void shouldNotFindByIdWhenInvalidId() {
    // given

    // when
    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> productService.findById(1L)).isInstanceOf(ProductNotFoundException.class);
    verify(productRepository).findById(1L);
    verifyNoInteractions(productModelAssembler);
  }

  @Test
  void shouldCreate() {
    // given
    given(productMapper.toDomain(productRequestDto)).willReturn(product1);
    given(productRepository.save(product1)).willReturn(product1);
    given(productModelAssembler.toModel(product1)).willReturn(productDto1);

    // when
    ProductDto response = productService.create(productRequestDto);

    // then
    then(response).usingRecursiveComparison().isEqualTo(productDto1);
    verify(productMapper).toDomain(productRequestDto);
    verify(productRepository).save(product1);
    verify(productModelAssembler).toModel(product1);
  }

  @Test
  void shouldReplace() throws MalformedURLException {
    // given
    Product changedProduct = new Product();
    changedProduct.setId(2L);
    changedProduct.setName("Product 1");
    changedProduct.setDescription("Description 1");
    changedProduct.setCategory(category);
    changedProduct.setMainImage(new URL("https://www.google.com"));

    ProductDto changedProductDto =
        new ProductDto(
            2L, "Product 1", "Description 1", "Category 1", new URL("https://www.google.com"));

    given(productMapper.toDomain(productRequestDto)).willReturn(product1);
    given(productRepository.findById(2L)).willReturn(Optional.of(product2));
    given(productRepository.save(changedProduct)).willReturn(changedProduct);
    given(productModelAssembler.toModel(changedProduct)).willReturn(changedProductDto);

    // when
    ProductDto response = productService.replace(2L, productRequestDto);

    // then
    then(response).usingRecursiveComparison().isEqualTo(changedProductDto);
    verify(productMapper).toDomain(productRequestDto);
    verify(productRepository).findById(2L);
    verify(productRepository).save(changedProduct);
    verify(productModelAssembler).toModel(changedProduct);
  }

  @Test
  void shouldNotReplaceWhenInvalidId() {
    // given
    given(productMapper.toDomain(productRequestDto)).willReturn(product1);

    // when
    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> productService.replace(1L, productRequestDto))
        .isInstanceOf(ProductNotFoundException.class);
    verify(productMapper).toDomain(productRequestDto);
    verify(productRepository).findById(1L);
    verifyNoInteractions(productModelAssembler);
  }

  @Test
  void shouldDeleteById() {
    // given
    given(productRepository.findById(1L)).willReturn(Optional.of(product1));

    // when
    productService.deleteById(1L);

    // then
    verify(productRepository).findById(1L);
    verify(productRepository).delete(product1);
  }

  @Test
  void shouldNotDeleteByIdWhenInvalidId() {
    // given

    // when
    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> productService.deleteById(1L)).isInstanceOf(ProductNotFoundException.class);
    verify(productRepository).findById(1L);
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
    verify(productRepository).findById(1L);
    verify(categoryModelAssembler).toModel(category);
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
    verify(productRepository).findById(2L);
    verifyNoInteractions(categoryModelAssembler);
  }
}
