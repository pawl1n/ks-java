package ua.kishkastrybaie.controller.dto.mapper;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.kishkastrybaie.controller.dto.ProductDto;
import ua.kishkastrybaie.controller.dto.ProductRequestDto;
import ua.kishkastrybaie.repository.entity.Category;
import ua.kishkastrybaie.repository.entity.Product;

@ExtendWith(MockitoExtension.class)
class ProductMapperTest {

  @Mock private CategoryIdToCategoryMapperImpl categoryIdToCategoryMapperImpl;
  @InjectMocks private ProductMapperImpl productMapper;

  @Test
  void shouldMapToDto() throws MalformedURLException {
    // given
    Category category = new Category();
    category.setId(1L);
    category.setName("category");

    Product product = new Product();
    product.setId(1L);
    product.setName("name");
    product.setDescription("description");
    product.setCategory(category);
    product.setMainImage(new URL("https://www.google.com"));

    // when

    // then
    then(productMapper.toDto(product))
        .usingRecursiveComparison()
        .isEqualTo(
            new ProductDto(
                1L, "name", "description", "category", new URL("https://www.google.com")));
  }

  @Test
  void shouldMapToDomain() throws MalformedURLException {
    // given
    Category category = new Category();
    category.setId(1L);
    category.setName("category");

    ProductRequestDto productRequestDto =
        new ProductRequestDto("name", "description", 1L, new URL("https://www.google.com"));

    // when
    when(categoryIdToCategoryMapperImpl.toCategory(1L)).thenReturn(category);

    // then
    Product expectedProduct = new Product();
    expectedProduct.setName("name");
    expectedProduct.setDescription("description");
    expectedProduct.setMainImage(new URL("https://www.google.com"));
    expectedProduct.setCategory(category);

    then(productMapper.toDomain(productRequestDto))
        .isNotNull()
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(expectedProduct);
  }
}
