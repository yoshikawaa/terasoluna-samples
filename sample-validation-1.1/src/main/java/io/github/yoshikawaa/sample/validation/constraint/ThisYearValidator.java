package io.github.yoshikawaa.sample.validation.constraint;

import java.util.Calendar;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.spi.time.TimeProvider;

public class ThisYearValidator implements ConstraintValidator<ThisYear, Date> {

    @Override
    public void initialize(ThisYear constraintAnnotation) {
    }

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        Calendar valueCal = Calendar.getInstance();
        valueCal.setTime(value);
        
        TimeProvider timeProvider = context.unwrap(HibernateConstraintValidatorContext.class).getTimeProvider();
        Calendar referenceCal = Calendar.getInstance();
        referenceCal.setTime(new Date(timeProvider.getCurrentTime()));
        
        return valueCal.get(Calendar.YEAR) == referenceCal.get(Calendar.YEAR);
    }

}
