package io.github.yoshikawaa.sample.validation;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import javax.validation.ClockProvider;

import org.springframework.beans.factory.InitializingBean;

import lombok.Setter;

public class FixedClockProvider implements ClockProvider, InitializingBean {

    @Setter
    private String instantPattern;
    
    @Setter
    private String zoneIdPattern;
    
    private Clock clock;
    
    @Override
    public Clock getClock() {
        return clock;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Instant instant = instantPattern == null ? Instant.now() : Instant.parse(instantPattern);
        ZoneId zoneId = zoneIdPattern == null ? ZoneId.systemDefault() : ZoneId.of(zoneIdPattern);
        clock = Clock.fixed(instant, zoneId);
    }

}
