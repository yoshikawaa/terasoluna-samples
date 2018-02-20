package io.github.yoshikawaa.sample.app.common.constraint;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SupportedBrowserValidator implements ConstraintValidator<SupportedBrowser, CharSequence> {

    private String browser;
    
    @Override
    public void initialize(SupportedBrowser constraintAnnotation) {
        this.browser = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        return Objects.equals(value, browser);
    }

}
