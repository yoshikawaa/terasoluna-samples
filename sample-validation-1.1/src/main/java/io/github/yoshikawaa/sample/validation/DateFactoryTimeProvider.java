package io.github.yoshikawaa.sample.validation;

import org.hibernate.validator.spi.time.TimeProvider;
import org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory;

import lombok.Setter;

public class DateFactoryTimeProvider implements TimeProvider {

    @Setter
    private JodaTimeDateFactory dateFactory;
    
    @Override
    public long getCurrentTime() {
        return dateFactory.newDateTime().getMillis();
    }

}
