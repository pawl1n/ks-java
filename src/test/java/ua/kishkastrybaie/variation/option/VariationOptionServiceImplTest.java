package ua.kishkastrybaie.variation.option;

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
import ua.kishkastrybaie.variation.Variation;
import ua.kishkastrybaie.variation.VariationNotFoundException;
import ua.kishkastrybaie.variation.VariationRepository;

@ExtendWith(MockitoExtension.class)
class VariationOptionServiceImplTest {
  Variation variation;
  VariationOption variationOption1;
  VariationOption variationOption2;
  VariationOptionDto variationOptionDto1;
  VariationOptionDto variationOptionDto2;
  VariationOptionRequestDto variationOptionRequestDto1;

  @Mock private VariationOptionModelAssembler representationModelAssembler;
  @Mock private VariationOptionRepository variationOptionRepository;
  @Mock private VariationRepository variationRepository;
  @Mock private PagedResourcesAssembler<VariationOption> pagedResourcesAssembler;
  @InjectMocks private VariationOptionServiceImpl variationOptionServiceImpl;

  @BeforeEach
  void setUp() {
    variation = new Variation();
    variation.setId(1L);
    variation.setName("name");

    variationOption1 = new VariationOption();
    variationOption1.setVariation(variation);
    variationOption1.setValue("value1");

    variationOption2 = new VariationOption();
    variationOption2.setVariation(variation);
    variationOption2.setValue("value2");

    variationOptionDto1 =
        new VariationOptionDto(
            variationOption1.getVariation().getId(), variationOption1.getValue());

    variationOptionDto2 =
        new VariationOptionDto(
            variationOption2.getVariation().getId(), variationOption2.getValue());

    variationOptionRequestDto1 = new VariationOptionRequestDto(variationOption1.getValue());
  }

  @Test
  void shouldFindAllByVariationId() {
    // given
    Page<VariationOption> variationOptions =
        new PageImpl<>(List.of(variationOption1, variationOption2));
    PagedModel<VariationOptionDto> variationOptionDtoCollectionModel =
        PagedModel.of(
            List.of(variationOptionDto1, variationOptionDto2),
            new PagedModel.PageMetadata(5, 0, 2));

    given(variationRepository.existsById(1L)).willReturn(true);
    given(variationOptionRepository.findAllByVariationId(1L, PageRequest.ofSize(5)))
        .willReturn(variationOptions);
    given(pagedResourcesAssembler.toModel(variationOptions, representationModelAssembler))
        .willReturn(variationOptionDtoCollectionModel);

    // when
    CollectionModel<VariationOptionDto> actualResult =
        variationOptionServiceImpl.findAllByVariationId(1L, PageRequest.ofSize(5));

    // then
    then(actualResult).isEqualTo(variationOptionDtoCollectionModel);
  }

  @Test
  void shouldNotFindAllByVariationIdWhenInvalidId() {
    // given

    // when
    when(variationRepository.existsById(1L)).thenReturn(false);

    // then
    thenThrownBy(() -> variationOptionServiceImpl.findAllByVariationId(1L, PageRequest.ofSize(5)))
        .isInstanceOf(VariationNotFoundException.class);
    verify(variationRepository).existsById(1L);
    verifyNoInteractions(variationOptionRepository);
    verifyNoInteractions(pagedResourcesAssembler);
  }

  @Test
  void shouldFindByVariationIdAndValue() {
    // given
    given(variationRepository.existsById(1L)).willReturn(true);
    given(variationRepository.getReferenceById(1L)).willReturn(variation);
    given(variationOptionRepository.findById(new VariationOptionId(variation, "value1")))
        .willReturn(Optional.of(variationOption1));
    given(representationModelAssembler.toModel(variationOption1)).willReturn(variationOptionDto1);

    // when
    VariationOptionDto actualResult =
        variationOptionServiceImpl.findByVariationIdAndValue(1L, "value1");

    // then
    then(actualResult).isEqualTo(variationOptionDto1);
  }

  @Test
  void shouldNotFindByVariationIdAndValueWhenInvalidVariationId() {
    // given

    // when
    when(variationRepository.existsById(1L)).thenReturn(false);

    // then
    thenThrownBy(() -> variationOptionServiceImpl.findByVariationIdAndValue(1L, "value"))
        .isInstanceOf(VariationNotFoundException.class);
    verify(variationRepository).existsById(1L);
    verifyNoInteractions(variationOptionRepository);
    verifyNoInteractions(representationModelAssembler);
  }

  @Test
  void shouldNotFindByVariationIdAndValueWhenInvalidValue() {
    // given
    given(variationRepository.existsById(1L)).willReturn(true);
    given(variationRepository.getReferenceById(1L)).willReturn(variation);

    // when
    when(variationOptionRepository.findById(new VariationOptionId(variation, "value")))
        .thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> variationOptionServiceImpl.findByVariationIdAndValue(1L, "value"))
        .isInstanceOf(VariationOptionNotFoundException.class);
    verifyNoInteractions(representationModelAssembler);
  }

  @Test
  void shouldCreate() {
    // given
    given(variationRepository.existsById(1L)).willReturn(true);
    given(variationRepository.getReferenceById(1L)).willReturn(variation);
    given(
            variationOptionRepository.save(
                argThat(
                    variationOption ->
                        variationOption.getValue().equals(variationOption1.getValue())
                            && variationOption
                                .getVariation()
                                .equals(variationOption1.getVariation()))))
        .willReturn(variationOption1);
    given(representationModelAssembler.toModel(variationOption1)).willReturn(variationOptionDto1);

    // when
    VariationOptionDto actualResult =
        variationOptionServiceImpl.create(1L, variationOptionRequestDto1);

    // then
    then(actualResult).isEqualTo(variationOptionDto1);
  }

  @Test
  void shouldNotCreateWhenInvalidVariationId() {
    // given

    // when
    when(variationRepository.existsById(1L)).thenReturn(false);

    // then
    thenThrownBy(() -> variationOptionServiceImpl.create(1L, variationOptionRequestDto1))
        .isInstanceOf(VariationNotFoundException.class);
    verify(variationRepository).existsById(1L);
    verifyNoInteractions(variationOptionRepository);
    verifyNoInteractions(representationModelAssembler);
  }

  @Test
  void shouldReplace() {
    // given
    given(variationRepository.existsById(1L)).willReturn(true);
    given(variationRepository.getReferenceById(1L)).willReturn(variation);
    given(variationOptionRepository.findById(new VariationOptionId(variation, "value")))
        .willReturn(Optional.of(variationOption2));
    given(
            variationOptionRepository.save(
                argThat(
                    variationOption ->
                        variationOption.getValue().equals(variationOption1.getValue())
                            && variationOption
                                .getVariation()
                                .equals(variationOption1.getVariation()))))
        .willReturn(variationOption1);
    given(representationModelAssembler.toModel(variationOption1)).willReturn(variationOptionDto1);

    // when
    VariationOptionDto actualResult =
        variationOptionServiceImpl.replace(1L, "value", variationOptionRequestDto1);

    // then
    then(actualResult).isEqualTo(variationOptionDto1);
  }

  @Test
  void shouldNotReplaceWhenInvalidVariationId() {
    // given

    // when
    when(variationRepository.existsById(1L)).thenReturn(false);

    // then
    thenThrownBy(() -> variationOptionServiceImpl.replace(1L, "value", variationOptionRequestDto1))
        .isInstanceOf(VariationNotFoundException.class);
    verify(variationRepository).existsById(1L);
    verifyNoInteractions(variationOptionRepository);
    verifyNoInteractions(representationModelAssembler);
  }

  @Test
  void shouldNotReplaceWhenInvalidVariationOptionId() {
    // given
    given(variationRepository.existsById(1L)).willReturn(true);
    given(variationRepository.getReferenceById(1L)).willReturn(variation);

    // when
    when(variationOptionRepository.findById(new VariationOptionId(variation, "value")))
        .thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> variationOptionServiceImpl.replace(1L, "value", variationOptionRequestDto1))
        .isInstanceOf(VariationOptionNotFoundException.class);
    verifyNoMoreInteractions(variationOptionRepository);
    verifyNoInteractions(representationModelAssembler);
  }

  @Test
  void shouldDelete() {
    // given
    given(variationRepository.existsById(1L)).willReturn(true);
    given(variationRepository.getReferenceById(1L)).willReturn(variation);
    given(variationOptionRepository.existsById(new VariationOptionId(variation, "value")))
        .willReturn(true);

    // when
    variationOptionServiceImpl.delete(1L, "value");

    // then
    verify(variationOptionRepository).deleteById(new VariationOptionId(variation, "value"));
    verifyNoMoreInteractions(variationRepository);
    verifyNoMoreInteractions(variationOptionRepository);
    verifyNoInteractions(representationModelAssembler);
  }

  @Test
  void shouldNotDeleteWhenInvalidVariationId() {
    // given

    // when
    when(variationRepository.existsById(1L)).thenReturn(false);

    // then
    thenThrownBy(() -> variationOptionServiceImpl.delete(1L, "value"))
        .isInstanceOf(VariationNotFoundException.class);
    verifyNoMoreInteractions(variationRepository);
    verifyNoInteractions(variationOptionRepository);
    verifyNoInteractions(representationModelAssembler);
  }

  @Test
  void shouldNotDeleteWhenInvalidVariationOptionId() {
    // given
    given(variationRepository.existsById(1L)).willReturn(true);
    given(variationRepository.getReferenceById(1L)).willReturn(variation);

    // when
    when(variationOptionRepository.existsById(new VariationOptionId(variation, "value")))
        .thenReturn(false);

    // then
    thenThrownBy(() -> variationOptionServiceImpl.delete(1L, "value"))
        .isInstanceOf(VariationOptionNotFoundException.class);
    verifyNoMoreInteractions(variationRepository);
    verifyNoMoreInteractions(variationOptionRepository);
    verifyNoInteractions(representationModelAssembler);
  }
}
