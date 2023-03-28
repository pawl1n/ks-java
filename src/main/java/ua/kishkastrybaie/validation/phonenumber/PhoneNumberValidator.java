package ua.kishkastrybaie.validation.phonenumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.invoke.MethodHandles;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

@Slf4j
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, CharSequence> {
  private static final Log LOG = LoggerFactory.make(MethodHandles.lookup());
  private java.util.regex.Pattern pattern;

  @Override
  public void initialize(PhoneNumber parameters) {
    try {
      pattern = java.util.regex.Pattern.compile(parameters.regexp());
    } catch (PatternSyntaxException e) {
      log.error(e.getMessage(), e);
      throw LOG.getInvalidRegularExpressionException(e);
    }
  }

  @Override
  public boolean isValid(
      CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
    if (value == null) {
      return false;
    }

    Matcher m = pattern.matcher(value);
    return m.matches();
  }
}
