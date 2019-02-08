package io.github.yoshikawaa.sample.validation;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.hibernate.validator.spi.time.TimeProvider;
import org.springframework.beans.factory.InitializingBean;

import lombok.Setter;

public class FixedTimeProvider implements TimeProvider, InitializingBean {

    @Setter
    private String instantPattern;
    
    @Setter
    private String zoneIdPattern;
    
    private Clock clock;
    
    @Override
    public long getCurrentTime() {
        return clock.millis();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Instant instant = instantPattern == null ? Instant.now() : Instant.parse(instantPattern);
        ZoneId zoneId = zoneIdPattern == null ? ZoneId.systemDefault() : ZoneId.of(zoneIdPattern);
        clock = Clock.fixed(instant, zoneId);
    }

}
