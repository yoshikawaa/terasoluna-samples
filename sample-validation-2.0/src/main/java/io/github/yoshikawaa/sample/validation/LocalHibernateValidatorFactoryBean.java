package io.github.yoshikawaa.sample.validation;

import javax.validation.ClockProvider;
import javax.validation.Configuration;

import org.hibernate.validator.HibernateValidatorConfiguration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import lombok.Setter;

public class LocalHibernateValidatorFactoryBean extends LocalValidatorFactoryBean {

    @Setter
    private ClockProvider clockProvider;
    
    @Override
    protected void postProcessConfiguration(Configuration<?> configuration) {
        if (configuration instanceof HibernateValidatorConfiguration) {
            postProcessHibernateValidatorConfiguration((HibernateValidatorConfiguration) configuration);
        }
    }

    protected void postProcessHibernateValidatorConfiguration(HibernateValidatorConfiguration configuration) {
        configuration.clockProvider(clockProvider);
    }
}
