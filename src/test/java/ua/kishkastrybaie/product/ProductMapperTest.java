package ua.kishkastrybaie.product;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.kishkastrybaie.category.Category;
import ua.kishkastrybaie.image.Image;
import ua.kishkastrybaie.shared.mapper.CategoryIdToCategoryMapperImpl;

@ExtendWith(MockitoExtension.class)
class ProductMapperTest {

  @Mock private CategoryIdToCategoryMapperImpl categoryIdToCategoryMapperImpl;
  @Mock private ImageURLToImageMapperImpl imageURLToImageMapperImpl;
  @InjectMocks private ProductMapperImpl productMapper;

  @Test
  void shouldMapToDto() throws MalformedURLException {
    // given
    Category category = new Category();
    category.setId(1L);
    category.setName("category");

    Image image = new Image();
    image.setName("E4bu1cRXoAMRnXz.jpg");
    image.setUrl(new URL("https://pbs.twimg.com/media/E4bu1cRXoAMRnXz.jpg"));

    Product product = new Product();
    product.setId(1L);
    product.setName("name");
    product.setDescription("description");
    product.setCategory(category);
    product.setMainImage(image);

    // when

    // then
    then(productMapper.toDto(product))
        .usingRecursiveComparison()
        .isEqualTo(
            new ProductDto(
                1L,
                "name",
                "description",
                "category",
                new URL("https://pbs.twimg.com/media/E4bu1cRXoAMRnXz.jpg")));
  }

  @Test
  void shouldMapToDomain() throws MalformedURLException {
    // given
    Category category = new Category();
    category.setId(1L);
    category.setName("category");

    Image image = new Image();
    image.setName("E4bu1cRXoAMRnXz.jpg");
    image.setUrl(new URL("https://pbs.twimg.com/media/E4bu1cRXoAMRnXz.jpg"));

    ProductRequestDto productRequestDto =
        new ProductRequestDto(
            "name", "description", 1L, new URL("https://pbs.twimg.com/media/E4bu1cRXoAMRnXz.jpg"));

    // when
    when(categoryIdToCategoryMapperImpl.toCategory(1L)).thenReturn(category);
    when(imageURLToImageMapperImpl.toImage(productRequestDto.mainImage())).thenReturn(image);

    // then

    Product expectedProduct = new Product();
    expectedProduct.setName("name");
    expectedProduct.setDescription("description");
    expectedProduct.setMainImage(image);
    expectedProduct.setCategory(category);

    then(productMapper.toDomain(productRequestDto))
        .isNotNull()
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(expectedProduct);

    verify(categoryIdToCategoryMapperImpl).toCategory(1L);
    verify(imageURLToImageMapperImpl).toImage(productRequestDto.mainImage());
  }
}
