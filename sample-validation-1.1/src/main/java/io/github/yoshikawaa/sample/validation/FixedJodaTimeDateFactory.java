package io.github.yoshikawaa.sample.validation;

import org.joda.time.DateTime;
import org.springframework.beans.factory.InitializingBean;
import org.terasoluna.gfw.common.date.jodatime.AbstractJodaTimeDateFactory;

import lombok.Setter;

public class FixedJodaTimeDateFactory extends AbstractJodaTimeDateFactory implements InitializingBean {

    @Setter
    private String pattern;

    private DateTime fixedDateTime;
    
    @Override
    public DateTime newDateTime() {
        return fixedDateTime;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        fixedDateTime = pattern == null ? DateTime.now() : DateTime.parse(pattern);
    }

}
