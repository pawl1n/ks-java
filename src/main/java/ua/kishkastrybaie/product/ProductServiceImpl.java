package ua.kishkastrybaie.product;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kishkastrybaie.category.*;
import ua.kishkastrybaie.image.*;
import ua.kishkastrybaie.shared.AuthorizationService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;
  private final CategoryModelAssembler categoryModelAssembler;
  private final ProductModelAssembler productModelAssembler;
  private final PagedResourcesAssembler<Product> pagedResourcesAssembler;
  private final ImageModelAssembler imageModelAssembler;
  private final AuthorizationService authorizationService;
  private final CategoryRepository categoryRepository;
  private final ImageRepository imageRepository;

  @Override
  public CollectionModel<ProductDto> findAll(Pageable pageable) {
    if (authorizationService.isAdmin()) {
      return pagedResourcesAssembler.toModel(
          productRepository.findAll(pageable), productModelAssembler);
    }

    return pagedResourcesAssembler.toModel(
        productRepository.findAllByProductItemsIsNotNull(pageable), productModelAssembler);
  }

  @Override
  public ProductDto findById(Long id) {
    return productModelAssembler.toModel(
        productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id)));
  }

  @Override
  public ProductDto create(ProductRequestDto productRequestDto) {
    Category category = getCategory(productRequestDto.category());
    Image image = getImage(productRequestDto.mainImage());

    Product product = new Product();
    product.setDescription(productRequestDto.description());
    product.setName(productRequestDto.name());
    product.setCategory(category);
    product.setMainImage(image);

    Product saved = productRepository.save(product);

    return productModelAssembler.toModel(saved);
  }

  @Override
  public ProductDto replace(Long id, ProductRequestDto productRequestDto) {
    Category category = getCategory(productRequestDto.category());
    Image image = getImage(productRequestDto.mainImage());

    Product product =
        productRepository
            .findById(id)
            .map(
                p -> {
                  p.setName(productRequestDto.name());
                  p.setDescription(productRequestDto.description());
                  p.setCategory(category);
                  p.setMainImage(image);
                  return productRepository.save(p);
                })
            .orElseThrow(() -> new ProductNotFoundException(id));

    return productModelAssembler.toModel(product);
  }

  @Override
  public void deleteById(Long id) {
    Product product =
        productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    productRepository.delete(product);
  }

  @Override
  @Transactional
  public CategoryDto getProductCategory(Long id) {
    Product product =
        productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

    Category category =
        Optional.ofNullable(product.getCategory())
            .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

    return categoryModelAssembler.toModel(category);
  }

  @Override
  @Transactional
  public ImageDto getProductImage(Long id) {
    Product product =
        productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

    Image image =
        Optional.ofNullable(product.getMainImage())
            .orElseThrow(() -> new ImageNotFoundException("Image not found"));

    return imageModelAssembler.toModel(image);
  }

  private Category getCategory(Long id) {
    if (id != null) {
      if (!categoryRepository.existsById(id)) {
        throw new CategoryNotFoundException(id);
      }
      return categoryRepository.getReferenceById(id);
    }

    return null;
  }

  private Image getImage(URL url) {
    Image image = null;

    if (url != null) {
      if (!imageRepository.existsByUrl(url)) {
        image = new Image();
        String filename = Paths.get(url.getPath()).getFileName().toString();
        String filenameWithoutExtension = filename.substring(0, filename.lastIndexOf('.'));
        image.setName(filenameWithoutExtension);
        image.setUrl(url);
      } else {
        image = imageRepository.getReferenceByUrl(url);
      }
    }

    return image;
  }
}
