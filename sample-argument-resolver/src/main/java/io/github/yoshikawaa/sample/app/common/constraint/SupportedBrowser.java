package io.github.yoshikawaa.sample.app.common.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RUNTIME)
@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Constraint(validatedBy = { SupportedBrowserValidator.class })
public @interface SupportedBrowser {
    String message() default "{io.github.yoshikawaa.common.constraint.SupportedBrowser.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value();

    @Documented
    @Retention(RUNTIME)
    @Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
    public @interface List {
        SupportedBrowser[] value();
    }
}
