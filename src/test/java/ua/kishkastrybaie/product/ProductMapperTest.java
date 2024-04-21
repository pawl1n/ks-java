package ua.kishkastrybaie.product;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.kishkastrybaie.category.Category;
import ua.kishkastrybaie.category.CategoryDto;
import ua.kishkastrybaie.category.CategoryMapper;
import ua.kishkastrybaie.image.Image;

@ExtendWith(MockitoExtension.class)
class ProductMapperTest {
  @Mock private CategoryMapper categoryMapper;
  @InjectMocks private ProductMapperImpl productMapper;

  @Test
  void shouldMapToDto() throws URISyntaxException, MalformedURLException {
    // given
    Category category = new Category();
    category.setId(1L);
    category.setName("category");

    Image image = new Image();
    image.setName("E4bu1cRXoAMRnXz.jpg");
    image.setUrl(new URI("https://pbs.twimg.com/media/E4bu1cRXoAMRnXz.jpg").toURL());

    Product product = new Product();
    product.setId(1L);
    product.setName("name");
    product.setDescription("description");
    product.setCategory(category);
    product.setMainImage(image);
    product.setPrice(1000.0);

    CategoryDto categoryDto = new CategoryDto(1L, "category", null, null, null);

    // when
    when(categoryMapper.toDto(category)).thenReturn(categoryDto);

    // then
    then(productMapper.toDto(product))
        .usingRecursiveComparison()
        .isEqualTo(
            new ProductDto(
                1L,
                "name",
                "description",
                null,
                null,
                1000.0,
                categoryDto,
                new URI("https://pbs.twimg.com/media/E4bu1cRXoAMRnXz.jpg").toURL()));
  }
}
