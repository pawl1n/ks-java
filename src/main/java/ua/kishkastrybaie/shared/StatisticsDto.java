package ua.kishkastrybaie.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatisticsDto extends RepresentationModel<StatisticsDto> {
    Integer count;
    Double sum;
    Map<String, String> filters;
}
