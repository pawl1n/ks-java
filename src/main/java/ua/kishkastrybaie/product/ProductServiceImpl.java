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
import ua.kishkastrybaie.product.details.ProductDetailsDto;
import ua.kishkastrybaie.product.details.ProductDetailsModelAssembler;
import ua.kishkastrybaie.shared.SlugService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;
  private final CategoryModelAssembler categoryModelAssembler;
  private final ProductModelAssembler productModelAssembler;
  private final ProductDetailsModelAssembler productDetailsModelAssembler;
  private final PagedResourcesAssembler<Product> pagedResourcesAssembler;
  private final ImageModelAssembler imageModelAssembler;
  private final CategoryRepository categoryRepository;
  private final ImageRepository imageRepository;

  @Override
  public CollectionModel<ProductDto> findAll(Pageable pageable) {
    return pagedResourcesAssembler.toModel(
        productRepository.findAll(pageable), productModelAssembler);
  }

  @Override
  public ProductDto findById(Long id) {
    return productModelAssembler.toModel(
        productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id)));
  }

  @Override
  @Transactional
  public ProductDto findBySlug(String slug) {
    return productModelAssembler.toModel(
        productRepository
            .findBySlug(slug)
            .orElseThrow(
                () -> new ProductNotFoundException("Product not found with slug: " + slug)));
  }

  @Override
  @Transactional
  public ProductDetailsDto getDetailsBySlug(String slug) {
    return productDetailsModelAssembler.toModel(
        productRepository
            .findBySlug(slug)
            .orElseThrow(
                () -> new ProductNotFoundException("Product not found with slug: " + slug)));
  }

  @Override
  @Transactional
  public CollectionModel<ProductDto> findByCategoryPath(String path, Pageable pageable) {
    if (path == null) {
      throw new IllegalArgumentException("Path cannot be null");
    }

    String ltreePath = path.replace("/", ".").replace("-", "_");
    if (ltreePath.startsWith(".")) {
      ltreePath = ltreePath.substring(1);
    }

    Category category =
        categoryRepository
            .findByPath(ltreePath)
            .orElseThrow(
                () -> new CategoryNotFoundException("Category not found with path: " + path));

    return pagedResourcesAssembler.toModel(
        productRepository.findAllByCategory(category, pageable), productModelAssembler);
  }

  @Override
  @Transactional
  public ProductDto create(ProductRequestDto productRequestDto) {
    Category category = getCategory(productRequestDto.category());
    Image image = getImage(productRequestDto.mainImage());

    Product product = new Product();
    product.setDescription(productRequestDto.description());
    product.setName(productRequestDto.name());
    product.setCategory(category);
    product.setMainImage(image);
    product.setSlug(generateSlug(productRequestDto));

    Product saved = productRepository.save(product);

    return productModelAssembler.toModel(saved);
  }

  @Override
  @Transactional
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
                  p.setSlug(generateSlug(productRequestDto));
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

  private String generateSlug(ProductRequestDto productRequestDto) {
    String textToSlugify;
    if (productRequestDto.slug() != null) {
      textToSlugify = productRequestDto.slug();
    } else {
      textToSlugify = productRequestDto.name();
    }

    return SlugService.slugify(textToSlugify);
  }
}
