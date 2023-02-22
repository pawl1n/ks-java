package ua.kishkastrybaie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.kishkastrybaie.controller.dto.ErrorDto;
import ua.kishkastrybaie.controller.dto.ProductItemDto;
import ua.kishkastrybaie.exception.VariationNotFoundException;
import ua.kishkastrybaie.repository.entity.ProductItem;
import ua.kishkastrybaie.service.ProductItemService;

@RestController
@RequestMapping("/api/v1/product-variations")
@RequiredArgsConstructor
public class ProductItemController {
    private final ProductItemService productItemService;
    private final RepresentationModelAssembler<ProductItem, ProductItemDto> productItemModelAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<ProductItemDto>> all() {
        CollectionModel<ProductItemDto> responseDto = productItemModelAssembler.toCollectionModel(productItemService.findAll());
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductItemDto> one(@PathVariable Long id) {
        ProductItemDto responseDto = productItemModelAssembler.toModel(productItemService.findById(id));
        return ResponseEntity.ok(responseDto);
    }

    @ExceptionHandler(VariationNotFoundException.class)
    public ResponseEntity<ErrorDto> handleVariationNotfound(VariationNotFoundException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
