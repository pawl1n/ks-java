package ua.kishkastrybaie.product;

import org.mapstruct.*;
import ua.kishkastrybaie.category.Category;
import ua.kishkastrybaie.category.CategoryMapper;
import ua.kishkastrybaie.product.details.ProductDetailsDto;
import ua.kishkastrybaie.product.item.ProductItemMapper;
import ua.kishkastrybaie.shared.BreadCrumb;

@Mapper(
    componentModel = "spring",
    uses = {CategoryMapper.class, ProductItemMapper.class},
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductMapper {
  @Named("toDto")
  @Mapping(source = "mainImage.url", target = "mainImage")
  ProductDto toDto(Product product);

//  @Mapping(source = "mainImage.url", target = "product.mainImage")
@Mapping(target = "product", qualifiedByName = "toDto")
@Mapping(source = "productItems", target = "variations")
@Mapping(target = "breadcrumbs", source = "category", qualifiedByName = "constructBreadcrumbs")
ProductDetailsDto toDetailsDto(Product product);

  @Named("constructBreadcrumbs")
  default BreadCrumb toBreadCrumb(Category category) {
    if (category == null) {
      return null;
    }

    BreadCrumb breadCrumb = null;
    Category current = category;

    while (current != null) {
      String path = current.getPath().replace(".", "/");

      breadCrumb = new BreadCrumb(current.getName(), path, breadCrumb);
      current = current.getParentCategory();
    }

    return breadCrumb;
  }
}
