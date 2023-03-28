package ua.kishkastrybaie.validation;

import java.util.Map;
import lombok.Getter;
import ua.kishkastrybaie.shared.ErrorDto;

@Getter
public class ValidationErrorDto extends ErrorDto {
  private final Map<String, String> fields;

  public ValidationErrorDto(String message, Map<String, String> fields) {
    super(message);
    this.fields = fields;
  }
}
