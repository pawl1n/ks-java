package ua.kishkastrybaie.variation;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;

@ExtendWith(MockitoExtension.class)
class VariationServiceImplTest {
  private static Variation variation1;
  private static Variation variation2;
  private static VariationDto variationDto1;
  private static VariationDto variationDto11;
  private static VariationRequestDto variationRequestDto;
  @Mock private VariationRepository variationRepository;
  @Mock private VariationModelAssembler variationModelAssembler;
  @Mock private PagedResourcesAssembler<Variation> pagedResourcesAssembler;
  @InjectMocks private VariationServiceImpl variationService;

  @BeforeEach
  void setUp() {
    variation1 = new Variation();
    variation1.setId(1L);
    variation1.setName("Variation 1");

    variation2 = new Variation();
    variation2.setId(2L);
    variation2.setName("Variation 2");

    variationDto1 = new VariationDto(1L, "Variation 1");

    variationDto11 = new VariationDto(2L, "Variation 2");

    variationRequestDto = new VariationRequestDto("Variation 1");
  }

  @Test
  void shouldFindAll() {
    // given
    Page<Variation> variations = new PageImpl<>(List.of(variation1, variation2));
    PagedModel<VariationDto> variationDtoCollectionModel =
        PagedModel.of(List.of(variationDto1, variationDto11), new PagedModel.PageMetadata(5, 0, 2));

    given(variationRepository.findAll(PageRequest.ofSize(5))).willReturn(variations);
    given(pagedResourcesAssembler.toModel(variations, variationModelAssembler))
        .willReturn(variationDtoCollectionModel);

    // when
    CollectionModel<VariationDto> response = variationService.findAll(PageRequest.ofSize(5));

    // then
    then(response).hasSize(2).usingRecursiveComparison().isEqualTo(variationDtoCollectionModel);
  }

  @Test
  void shouldFindById() {
    // given
    given(variationRepository.findById(1L)).willReturn(Optional.of(variation1));
    given(variationModelAssembler.toModel(variation1)).willReturn(variationDto1);

    // when
    VariationDto response = variationService.findById(1L);

    // then
    then(response).usingRecursiveComparison().isEqualTo(variationDto1);
    verify(variationRepository).findById(1L);
    verify(variationModelAssembler).toModel(variation1);
  }

  @Test
  void shouldNotFindByIdWhenInvalidId() {
    // given

    // when
    when(variationRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> variationService.findById(1L))
        .isInstanceOf(VariationNotFoundException.class);
    verify(variationRepository).findById(1L);
    verifyNoInteractions(variationModelAssembler);
  }

  @Test
  void shouldCreate() {
    // given
    given(variationRepository.save(argThat(variation -> variation.getName().equals(variation1.getName())))).willReturn(variation1);
    given(variationModelAssembler.toModel(variation1)).willReturn(variationDto1);

    // when
    VariationDto response = variationService.create(variationRequestDto);

    // then
    then(response).usingRecursiveComparison().isEqualTo(variationDto1);
  }

  @Test
  void shouldReplace() {
    // given
    Variation changedVariation = new Variation();
    changedVariation.setId(2L);
    changedVariation.setName("Variation 1");

    VariationDto changedVariationDto = new VariationDto(2L, "Variation 1");

    given(variationRepository.findById(2L)).willReturn(Optional.of(variation2));
    given(
            variationRepository.save(
                argThat(
                    Variation ->
                        Variation.getId().equals(2L)
                            && Variation.getName().equals(variationRequestDto.name()))))
        .willReturn(changedVariation);
    given(variationModelAssembler.toModel(changedVariation)).willReturn(changedVariationDto);

    // when
    VariationDto response = variationService.replace(2L, variationRequestDto);

    // then
    then(response).usingRecursiveComparison().isEqualTo(changedVariationDto);
    verify(variationRepository).findById(2L);
    verify(variationRepository).save(any());
    verify(variationModelAssembler).toModel(changedVariation);
  }

  @Test
  void shouldNotReplaceWhenInvalidId() {
    // given

    // when
    when(variationRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> variationService.replace(1L, variationRequestDto))
        .isInstanceOf(VariationNotFoundException.class);
    verify(variationRepository).findById(1L);
    verifyNoInteractions(variationModelAssembler);
  }

  @Test
  void shouldDeleteById() {
    // given
    given(variationRepository.existsById(1L)).willReturn(true);

    // when
    variationService.deleteById(1L);

    // then
    verify(variationRepository).existsById(1L);
    verify(variationRepository).deleteById(1L);
  }

  @Test
  void shouldNotDeleteByIdWhenInvalidId() {
    // given

    // when
    when(variationRepository.existsById(1L)).thenReturn(false);

    // then
    thenThrownBy(() -> variationService.deleteById(1L))
        .isInstanceOf(VariationNotFoundException.class);
    verify(variationRepository).existsById(1L);
    verifyNoInteractions(variationModelAssembler);
  }
}
