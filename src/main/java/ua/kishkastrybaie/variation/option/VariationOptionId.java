package ua.kishkastrybaie.variation.option;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ua.kishkastrybaie.variation.Variation;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class VariationOptionId implements Serializable {
    private transient Variation variation;
    private String value;
}
