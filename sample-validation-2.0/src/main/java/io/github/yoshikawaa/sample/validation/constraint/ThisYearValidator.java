package io.github.yoshikawaa.sample.validation.constraint;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.validation.ConstraintValidatorContext;
import javax.validation.metadata.ConstraintDescriptor;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidator;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorInitializationContext;

public class ThisYearValidator implements HibernateConstraintValidator<ThisYear, LocalDate> {

    private Clock clock;
    
    @Override
    public void initialize(ConstraintDescriptor<ThisYear> constraintDescriptor,
            HibernateConstraintValidatorInitializationContext initializationContext) {
        clock = initializationContext.getClockProvider().getClock();
    }
    
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        LocalDate referenceDate = LocalDateTime.ofInstant(clock.instant(), ZoneId.systemDefault()).toLocalDate();
        return value.getYear() == referenceDate.getYear();
    }

}
