package ua.kishkastrybaie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.kishkastrybaie.controller.dto.ErrorDto;
import ua.kishkastrybaie.controller.dto.VariationOptionDto;
import ua.kishkastrybaie.exception.VariationNotFoundException;
import ua.kishkastrybaie.repository.entity.VariationOption;
import ua.kishkastrybaie.service.VariationOptionService;

@RestController
@RequestMapping("/api/v1/variation-options")
@RequiredArgsConstructor
public class VariationOptionController {
    private final VariationOptionService variationOptionService;
    private final RepresentationModelAssembler<VariationOption, VariationOptionDto> variationOptionModelAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<VariationOptionDto>> all() {
        CollectionModel<VariationOptionDto> responseDto = variationOptionModelAssembler.toCollectionModel(variationOptionService.findAll());
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VariationOptionDto> one(@PathVariable Long id) {
        VariationOptionDto responseDto = variationOptionModelAssembler.toModel(variationOptionService.findById(id));
        return ResponseEntity.ok(responseDto);
    }

    @ExceptionHandler(VariationNotFoundException.class)
    public ResponseEntity<ErrorDto> handleVariationNotfound(VariationNotFoundException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
